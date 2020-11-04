package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IntelAsmOutput implements IRVisitor {


    private final LocationResolver resolver;
    private final MasmOutput out;
    private int labelsCount = 0;
    public Map<Inst, String> labelsMap = new HashMap<>();
    private final StrConst formatDecimal = new StrConst("%d");

    public IntelAsmOutput() {
        this.out = new MasmOutput();
        this.resolver = new LocationResolver(this.out);
    }


    public void generate(IRProgram program, String outputFilename) throws IOException {
        for (Symbol symbol : program.symbols()) {
            if (symbol.isInitialized()) {
                this.resolver.findMemLocationOrCreate(symbol);
            }
        }

        for (BranchInst branch : program.branches()) {
            Inst target = branch.getTarget();
            if (target != null) {
                target.markAsLeader();
                this.labelsCount++;
                String name = "label_" + this.labelsCount;
                this.labelsMap.put(target, name);
            }
        }

        for (Inst instr : program.instructions()) {
            if (instr.isLeader()) {
                this.out.addLabel(this.labelsMap.get(instr));
            }
            instr.accept(this);
        }

        for (MemLocation location : this.resolver.getGlobalLocations()) {
            this.out.addDataDeclaration(new MemDeclaration(location));
        }

        this.out.generate(outputFilename);
    }

    private void printDebug(Inst inst) {
        //StringBuilder str = new StringBuilder();
        //for (RegLocation reg : this.resolver.getAvailableRegisters()) {
        //    str.append(reg.toString()).append(" ");
        //}
        this.out.addComment("===> " + inst.toString()); // + ". Available Registers: " + str.toString());
    }

    @Override
    public void addInst(AddInst instr) {
        Location left = resolver.resolveOrCreate(instr.getLeft());
        Location right = resolver.resolveOrCreate(instr.getRight());

        if (left.isReg()) { // (reg, reg) or (reg, mem)
            this.out.addCode("add", left, right);
            left.setContent(instr);
        } else {
            if (right.isReg()) { // (mem, reg)
                RegLocation reg = (RegLocation) right;
                this.out.addCode("add", reg, left);
                reg.setContent(instr);
            } else { // (mem, mem)
                RegLocation reg = resolver.resolveOrCreateToReg(instr.getRight());
                this.out.addCode("add", reg, left);
                reg.setContent(instr);
            }
        }

        this.out.addJump("jo", this.out.getOverflowErrorLabel());
    }

    @Override
    public void subInst(SubInst instr) {
        Location left = resolver.resolveOrCreate(instr.getLeft());
        Location right = resolver.resolveOrCreate(instr.getRight());

        if (left.isReg()) {
            RegLocation leftReg = (RegLocation) left;
            if (right.isReg()) {
                // Case (reg, reg)
                this.out.addCode("sub", leftReg, left);
                leftReg.setContent(instr);
            } else {
                // Case (reg, mem)
                RegLocation reg = resolver.resolveOrCreateToReg(instr.getRight());
                this.out.addCode("sub", leftReg, reg);
                leftReg.setContent(instr);
                reg.markAsFree();
            }
        } else {
            if (right.isReg()) {
                // Case (mem, reg)
                RegLocation rightReg = (RegLocation) right;
                RegLocation reg = resolver.resolveOrCreateToReg(instr.getLeft());
                this.out.addCode("sub", reg, rightReg);
                reg.setContent(instr);
                rightReg.markAsFree();
            } else {
                // Case (mem, mem)
                RegLocation regLeft = resolver.resolveOrCreateToReg(instr.getLeft());
                RegLocation regRight = resolver.resolveOrCreateToReg(instr.getRight());
                this.out.addCode("sub", regLeft, regRight);
                regLeft.setContent(instr);
                regRight.markAsFree();
            }
        }
    }

    @Override
    public void mulInst(MulInst instr) {
        Location left = resolver.resolveOrCreate(instr.getLeft());
        Location right = resolver.resolveOrCreate(instr.getRight());
        this.resolver.spillRegIfNecessary(this.resolver.REG_DX);
        if (left == this.resolver.REG_AX) {
            // Case (eax, ???)
            this.out.addCode("mul", right);
            left.setContent(instr);
        } else if (right == this.resolver.REG_AX) {
            // Case (???, eax)
            this.out.addCode("mul", left);
            right.setContent(instr);
        } else {
            // Case (???, ???)
            RegLocation targetReg = this.resolver.resolveOrCreateToReg(instr.getLeft(), this.resolver.REG_AX);
            this.out.addCode("mul", right);
            targetReg.setContent(instr);
        }
    }

    @Override
    public void divInst(DivInst instr) {
        RegLocation left = resolver.resolveOrCreateToReg(instr.getLeft(), this.resolver.REG_AX);
        RegLocation right = resolver.resolveOrCreateToReg(instr.getRight());

        this.out.addCode("test", right, right);
        this.out.addJump("jz", this.out.getDivisionByZeroErrorLabel());

        if (right == this.resolver.REG_DX) {
            right = this.resolver.moveRegToAnyOtherReg(right);
        } else {
            this.resolver.spillRegIfNecessary(this.resolver.REG_DX);
        }
        this.clearReg(this.resolver.REG_DX);
        this.out.addCode("div", right);
        left.setContent(instr);
        right.markAsFree();
    }

    private void clearReg(RegLocation reg) {
        this.out.addCode("xor", reg, reg); // Clears dx
        reg.markAsFree();
    }

    @Override
    public void negInst(NegInst instr) {
        Location op = this.resolver.resolveOrCreateToReg(instr.getOp());
        this.out.addCode("neg", op);
        op.setContent(instr);
    }

    @Override
    public void itolInst(ItolInst instr) {
        this.convertToLong(instr.getOp()).setContent(instr);
    }

    public RegLocation convertToLong(Value value) {
        RegLocation targetReg = this.resolver.resolveOrCreateToReg(value, this.resolver.REG_AX);
        this.out.addCode("cwde");
        return targetReg;
    }

    @Override
    public void printInst(PrintInst instr) {
        this.resolver.preserveVolatileRegisters();
        Value value = instr.getValue();
        switch (value.getType()) {
            case LONG: {
                MemLocation format = this.resolver.findMemLocationOrCreate(this.formatDecimal);
                Location location = this.resolver.resolveOrFail(value);
                this.out.addPrint(format, location);
                location.markAsFree();
                break;
            }
            case INT: {
                MemLocation format = this.resolver.findMemLocationOrCreate(this.formatDecimal);
                RegLocation location = this.convertToLong(value);
                this.out.addPrint(format, location);
                location.markAsFree();
                break;
            }
            case STR: {
                this.out.addPrint(this.resolver.findMemLocationOrCreate(value));
                break;
            }
        }

    }

    private String condition(@Nullable BranchCondition condition) {
        if (condition == null) return "jmp";

        RegLocation loc1 = this.resolver.resolveOrCreateToReg(condition.getOp1());
        RegLocation loc2 = this.resolver.resolveOrCreateToReg(condition.getOp2());
        this.out.addCode("cmp", loc1, loc2);
        loc1.markAsFree();
        loc2.markAsFree();

        switch (condition.getOperator()) {
            case GT:    return "jg";
            case LT:    return "jl";
            case GTE:   return "jge";
            case LTE:   return "jle";
            case EQ:    return "je";
            case NOTEQ: return "jne";
        }
        return "jmp";
    }

    @Override
    public void branchInst(BranchInst instr) {
        BranchCondition condition = instr.getCondition();
        String type = this.condition(condition);
        this.out.addJump(type, this.labelsMap.get(instr.getTarget()));
    }

    @Override
    public void noopInst(NoOpInst instr) {
        // Nothing
    }

    @Override
    public void assignInst(AssignInst instr) {
        MemLocation dst = this.resolver.findMemLocationOrCreate(instr.getSymbol());
        RegLocation src = this.resolver.resolveOrCreateToReg(instr.getOp());
        this.out.addCode("mov", dst, src);
        src.markAsFree();
    }
}
