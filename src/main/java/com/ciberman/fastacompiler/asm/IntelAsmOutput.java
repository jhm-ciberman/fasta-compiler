package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class IntelAsmOutput implements IRVisitor {

    private final RegTable reg;
    private final MemTable mem;
    private final MasmOutput out;
    private int labelsCount = 0;
    public Map<Inst, String> labelsMap = new HashMap<>();

    public IntelAsmOutput() {
        this.mem = new MemTable();
        this.reg = new RegTable();
        this.out = new MasmOutput();
    }

    public @NotNull Location resolve(Value value) {
        Location loc = this.reg.findLocation(value);
        if (loc == null) {
            loc = this.mem.findLocation(value);
            if (loc == null) {
                throw new RuntimeException("Value " + value.toString() + " not declared");
            }
        }
        return loc;
    }

    private @NotNull RegLocation requestReg(Value value) {
        RegLocation loc = this.reg.requestReg(value);
        if (loc == null) {
            throw new RuntimeException("Not enough registers.");
        }
        return loc;
    }

    public @NotNull Location resolveToMemory(Symbol symbol) {
        Location loc = this.mem.findLocation(symbol);
        if (loc == null) {
            throw new RuntimeException("Value " + symbol.toString() + " not declared");
        }
        return loc;
    }

    public @NotNull RegLocation resolveToReg(Value value) {
        Location loc = this.resolve(value);
        if (! (loc instanceof RegLocation)) {
            RegLocation reg = this.requestReg(value);
            this.out.addCode("mov",  reg, loc);
            return reg;
        }
        return (RegLocation) loc;
    }


    public void generate(IRProgram program) {
        for (LongConst c : program.longConsts()) {
            String name = this.mem.reserveGlobalLocation(c).getName();
            this.out.addDataDeclaration(name + " dd " + c.getValue());
        }
        for (IntConst c : program.intConsts()) {
            String name = this.mem.reserveGlobalLocation(c).getName();
            this.out.addDataDeclaration(name + " dw " + c.getValue());
        }
        for (StrConst c : program.strConsts()) {
            String name = this.mem.reserveGlobalLocation(c).getName();
            this.out.addDataDeclaration(name + " db " + this.getStringConstValue(c));
        }
        for (Symbol s : program.symbols()) {
            String name = this.mem.reserveGlobalLocation(s).getName();
            String size = this.getSymbolSizeString(s);
            this.out.addDataDeclaration(name + " " + size + " 0");
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

        this.out.generate();
    }

    private String getSymbolSizeString(Symbol symbol) {
        switch (symbol.getType()) {
            case LONG: return "dd";
            case INT:  return "dw";
            case STR:  return "db";
        }
        return "db";
    }

    protected String getStringConstValue(StrConst strConst) {
        if (strConst.getValue().isEmpty())
            return "10, 0";
        return "'" + strConst.getValue() + "', 10, 0";
    }


    private static class LocationPair {

        private final Location dst;
        private final Location src;

        public LocationPair(Location dst, Location src) {
            this.dst = dst;
            this.src = src;
        }
    }

    public LocationPair resolveLocationPair(Location loc1, Location loc2, Value op1, Value op2) {

        if (! loc1.isReg() && ! loc2.isReg()) { // 2 memory values
            Location reg = this.requestReg(op1);
            this.out.addCode("mov", reg, loc1);
            return new LocationPair(reg, loc2);
        }

        return new LocationPair(loc1, loc2);
    }

    private LocationPair resolveLocationPairForBinInst(BinInst instr) {
        Location loc1 = this.resolve(instr.getOp1());
        Location loc2 = this.resolve(instr.getOp2());
        return this.resolveLocationPair(loc1, loc2, instr.getOp1(), instr.getOp2());
    }

    @Override
    public void addInst(AddInst instr) {
        LocationPair pair = this.resolveLocationPairForBinInst(instr);
        this.out.addCode("add", pair.dst, pair.src);
        pair.dst.setContent(instr);
    }

    @Override
    public void subInst(SubInst instr) {
        LocationPair pair = this.resolveLocationPairForBinInst(instr);
        this.out.addCode("sub", pair.dst, pair.src);
        pair.dst.setContent(instr);
    }

    @Override
    public void mulInst(MulInst instr) {
        // TODO: Add mul
    }

    @Override
    public void divInst(DivInst instr) {
        // TODO: Add div
    }

    @Override
    public void negInst(NegInst instr) {
        Location op = this.resolveToReg(instr.getOp());
        this.out.addCode("neg", op);
        op.setContent(instr);
    }

    @Override
    public void itolInst(ItolInst instr) {
        Location op = this.resolveToReg(instr.getOp());
        this.out.addCode("cwde", op);
        op.setContent(instr);
    }

    @Override
    public void printInst(PrintInst instr) {
        this.out.addPrint(this.resolve(instr.getString()));
    }

    private String condition(@Nullable BranchCondition condition) {
        if (condition == null) return "jmp";

        Location loc1 = this.resolveToReg(condition.getOp1());
        Location loc2 = this.resolveToReg(condition.getOp2());
        this.out.addCode("cmp", loc1, loc2);

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
        Location dst = this.resolveToMemory(instr.getSymbol());
        Location src = this.resolveToReg(instr.getOp());
        this.out.addCode("mov", dst, src);
    }
}
