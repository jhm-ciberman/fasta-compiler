package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmJump;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.asm.reg.RegLocation;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;

public class AsmDivTest extends AsmBase {
    @Test
    void divInstMemMem() {
        IntConst a = new IntConst(100);
        IntConst b = new IntConst(2);

        RegLocation ax = resolver.REG_AX.loc16();
        RegLocation bx = resolver.REG_BX.loc16();

        program.createDivInst(a, b);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", ax, resolver.saveInMem(a)),
                new AsmOp("mov", bx, resolver.saveInMem(b)),
                new AsmOp("test", bx, bx),
                new AsmJump("jz", LabelsTable.LABEL_DIVISION_BY_ZERO),
                new AsmOp("cwd"),
                new AsmOp("div", bx),
        });
    }
}
