package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmJump;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;

public class AsmAddTest extends AsmBase {
    @Test
    void addInstMemMem() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createAddInst(a, b);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.saveInMem(b)),
                new AsmOp("add", resolver.REG_AX.loc16(), resolver.saveInMem(a)),
                new AsmJump("jo", LabelsTable.LABEL_OVERFLOW)
        });
    }
}
