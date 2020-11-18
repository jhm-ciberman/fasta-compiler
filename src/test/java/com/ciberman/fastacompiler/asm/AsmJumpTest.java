package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.labels.Label;
import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmJump;
import com.ciberman.fastacompiler.asm.program.AsmLabel;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.asm.reg.RegLocation;
import com.ciberman.fastacompiler.ir.BranchCondition;
import com.ciberman.fastacompiler.ir.Inst;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class AsmJumpTest extends AsmBase {

    private static Stream<Arguments> getTestArgs() {
        return Stream.of(
                Arguments.of(BranchCondition.RelOperator.EQ, "je"),
                Arguments.of(BranchCondition.RelOperator.NOTEQ, "jne"),
                Arguments.of(BranchCondition.RelOperator.LT, "jl"),
                Arguments.of(BranchCondition.RelOperator.GT, "jg"),
                Arguments.of(BranchCondition.RelOperator.LTE, "jle"),
                Arguments.of(BranchCondition.RelOperator.GTE, "jge")
        );
    }

    @ParameterizedTest(name = "{index} => operator={0}, jumpType={1}")
    @MethodSource("getTestArgs")
    public void branchInstMemMem(BranchCondition.RelOperator operator, String jumpType) {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        BranchCondition condition = program.createBranchCondition(a, operator, b);
        Inst target = program.createNoOpInst();
        program.createBranchInst(condition, target);

        RegLocation ax = resolver.REG_AX.loc16();
        RegLocation bx = resolver.REG_BX.loc16();
        Label label = new Label("label_1");
        this.assertAsmEqual(new AsmCode[] {
                new AsmLabel(label),
                new AsmOp("mov", ax, resolver.saveInMem(a)),
                new AsmOp("mov", bx, resolver.saveInMem(b)),
                new AsmOp("cmp", ax, bx),
                new AsmJump(jumpType, label),
                new AsmOp("ret"),
        });
    }

    @Test
    public void branchUnconditional() {
        Inst target = program.createNoOpInst();
        program.createBranchInst(target);

        Label label = new Label("label_1");
        this.assertAsmEqual(new AsmCode[] {
                new AsmLabel(label),
                new AsmJump("jmp", label),
                new AsmOp("ret"),
        });
    }
}
