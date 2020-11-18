package com.ciberman.fastacompiler.asm.reg;

import com.ciberman.fastacompiler.asm.AsmBase;
import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;

class AsmSubTest extends AsmBase {

    @Test
    void subInstMemMem() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createSubInst(a, b);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.saveInMem(a)),
                new AsmOp("sub", resolver.REG_AX.loc16(), resolver.saveInMem(b)),
        });
    }
}
