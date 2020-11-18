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
        });
    }
}
