package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.ir.constants.IntConst;
import com.ciberman.fastacompiler.ir.constants.LongConst;
import com.ciberman.fastacompiler.ir.constants.StrConst;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class IRProgram implements Iterable<Inst> {

    private final List<Inst> instList = new ArrayList<>();

    private final Set<IntConst> intConsts = new HashSet<>();
    private final Set<LongConst> longConsts = new HashSet<>();
    private final Set<StrConst> strConsts = new HashSet<>();

    /**
     * Push an instruction into the instructions list
     * @param inst The instruction to push
     * @param <T> The type of the instruction
     * @return The same provided instruction, for easy chaining.
     */
    protected  <T extends Inst> T push(T inst) {
        this.instList.add(inst);
        return inst;
    }

    /**
     * Registers an integer constant
     * @param value The value
     * @return The constant
     */
    public IntConst createIntConst(int value) {
        IntConst c = new IntConst(value);
        this.intConsts.add(c);
        return c;
    }

    /**
     * Registers a long constant
     * @param value The value
     * @return The constant
     */
    public LongConst createLongConst(long value) {
        LongConst c = new LongConst(value);
        this.longConsts.add(c);
        return c;
    }

    /**
     * Registers a string constant
     * @param value The value
     * @return The constant
     */
    public StrConst createStrConst(@NotNull String value) {
        StrConst c = new StrConst(value);
        this.strConsts.add(c);
        return c;
    }

    public AddInst createAddInst(Value op1, Value op2) {
        return this.push(new AddInst(op1, op2));
    }

    public SubInst createSubInst(Value op1, Value op2) {
        return this.push(new SubInst(op1, op2));
    }

    public DivInst createDivInst(Value op1, Value op2) {
        return this.push(new DivInst(op1, op2));
    }

    public MulInst createMulInst(Value op1, Value op2) {
        return this.push(new MulInst(op1, op2));
    }

    public PrintInst createPrintInst(StrConst str) {
        return this.push(new PrintInst(str));
    }

    public @NotNull Iterator<IntConst> intConstIterator() {
        return this.intConsts.iterator();
    }

    public @NotNull Iterator<LongConst> longConstIterator() {
        return this.longConsts.iterator();
    }

    public @NotNull Iterator<StrConst> strConstIterator() {
        return this.strConsts.iterator();
    }

    public @NotNull Iterator<Inst> iterator() {
        return instList.iterator();
    }
}
