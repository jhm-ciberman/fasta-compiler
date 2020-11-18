package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;

public class AsmMulTest extends AsmBase {

    @Test
    void mulInstMemMem() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createMulInst(a, b);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.saveInMem(a)),
                new AsmOp("mul", resolver.saveInMem(b)),
                new AsmOp("ret"),
        });
    }

    @Test
    void mulInstMemRegA() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createMulInst(a, b);

        resolver.REG_AX.setContent(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mul", resolver.saveInMem(b)),
                new AsmOp("ret"),
        });
    }

    @Test
    void mulInstMemRegB() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createMulInst(a, b);

        resolver.REG_BX.setContent(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.REG_BX.loc16()),
                new AsmOp("mul", resolver.saveInMem(b)),
                new AsmOp("ret"),
        });
    }

    @Test
    void mulInstRegBRegC() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createMulInst(a, b);

        resolver.REG_BX.setContent(a);
        resolver.REG_CX.setContent(b);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.REG_BX.loc16()),
                new AsmOp("mul", resolver.REG_CX.loc16()),
                new AsmOp("ret"),
        });
    }

    @Test
    void mulInstShouldSpillRegD() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);
        IntConst data = new IntConst(123);

        program.createMulInst(a, b);

        resolver.REG_BX.setContent(a);
        resolver.REG_CX.setContent(b);
        resolver.REG_DX.setContent(data);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.REG_BX.loc16()),
                new AsmOp("mov", resolver.memLocationOrNew(data), resolver.REG_DX.loc16()),
                new AsmOp("mul", resolver.REG_CX.loc16()),
                new AsmOp("ret"),
        });
    }
}
