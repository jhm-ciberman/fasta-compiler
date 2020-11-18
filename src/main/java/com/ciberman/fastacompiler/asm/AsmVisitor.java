package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.labels.Label;
import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.asm.reg.RegLocation;
import com.ciberman.fastacompiler.ir.*;

class AsmVisitor implements IRVisitor {

    //private final Asm out;
    private final AsmBuilder builder;
    private final LocationResolver resolver;

    public AsmVisitor(AsmBuilder asmBuilder, LocationResolver resolver) {
        this.builder = asmBuilder;
        this.resolver = resolver;
    }

    public AsmBuilder getBuilder() {
        return this.builder;
    }

    public void processInstr(Inst instr) {
        if (instr.isLeader()) {
            this.builder.addLabel(this.resolver.getLabel(instr));
        }
        instr.accept(this);
    }

    @Override
    public void addInst(AddInst instr) {
        Location left = this.resolver.locationOrNew(instr.getLeft());
        Location right = this.resolver.locationOrNew(instr.getRight());

        if (left.isReg()) {
            // (reg, reg) or (reg, mem)
            this.builder.addAdd(instr, left, right);
        } else if (right.isReg()) {
            // (mem, reg)
            this.builder.addAdd(instr, right, left);
        } else {
            // (mem, mem)
            RegLocation reg = this.builder.moveToReg(instr.getRight());
            this.builder.addAdd(instr, reg, left);
        }
    }

    @Override
    public void subInst(SubInst instr) {
        Location left = this.resolver.locationOrNew(instr.getLeft());
        Location right = this.resolver.locationOrNew(instr.getRight());

        if (left.isReg()) {
            if (right.isReg()) {
                // Case (reg, reg)
                this.builder.addSub(instr, left, right);
            } else {
                // Case (reg, mem)
                RegLocation reg = this.builder.moveToReg(instr.getRight());
                this.builder.addSub(instr, left, reg);
            }
        } else {
            // Case (mem, ???)
            RegLocation reg = this.builder.moveToReg(instr.getLeft());
            this.builder.addSub(instr, reg, right);
        }
    }

    @Override
    public void mulInst(MulInst instr) {
        Location left = this.resolver.locationOrNew(instr.getLeft());
        Location right = this.resolver.locationOrNew(instr.getRight());

        if (right.isInReg(this.resolver.REG_AX)) {
            // Case (???, eax)
            this.builder.addMul(instr, left);
        } else if (left.isInReg(this.resolver.REG_AX)) {
            this.builder.addMul(instr, right);
        } else {
            // Case (???, ???)
            this.builder.moveToReg(left.getContent(), this.resolver.REG_AX);
            this.builder.addMul(instr, right);
        }
    }

    @Override
    public void divInst(DivInst instr) {
        this.builder.moveToReg(instr.getLeft(), this.resolver.REG_AX);
        RegLocation right = this.builder.moveToReg(instr.getRight());

        if (right.isInReg(this.resolver.REG_DX)) {
            right = this.builder.moveToAnyOtherReg(right);
        } else {
            this.builder.spillRegIfNecessary(this.resolver.REG_DX);
        }

        this.builder.addDiv(instr, right);
    }

    @Override
    public void negInst(NegInst instr) {
        Location location = this.builder.moveToReg(instr.getOp());
        this.builder.addNeg(instr, location);
    }

    @Override
    public void itolInst(ItolInst instr) {
        this.builder.moveToReg(instr.getOp(), this.resolver.REG_AX);
        this.builder.addItol(instr);
    }

    @Override
    public void printInst(PrintInst instr) {

        Value value = instr.getValue();
        switch (value.getType()) {
            case LONG:  this.builder.addPrintLong(this.resolver.location(value)); break;
            case INT:   this.builder.addPrintInt(this.resolver.location(value)); break;
            case STR:   this.builder.addPrintStr(this.resolver.memLocationOrNew(value)); break;
        }
    }

    @Override
    public void branchInst(BranchInst instr) {
        BranchCondition condition = instr.getCondition();
        Label label = this.resolver.getLabel(instr.getTarget());
        if (condition == null) {
            this.builder.addUnconditionalJump(label);
        } else {
            RegLocation loc1 = this.builder.moveToReg(condition.getOp1());
            RegLocation loc2 = this.builder.moveToReg(condition.getOp2());
            this.builder.addConditionalJump(condition.getOperator(), loc1, loc2, label);
        }
    }

    @Override
    public void noopInst(NoOpInst instr) {
        // Nothing
    }

    @Override
    public void assignInst(AssignInst instr) {
        MemLocation dst = this.resolver.memLocationOrNew(instr.getSymbol());
        RegLocation src = this.builder.moveToReg(instr.getOp());
        this.builder.addMov(instr, dst, src);
    }
}
