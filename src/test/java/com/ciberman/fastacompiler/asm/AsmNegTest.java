package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.asm.reg.RegLocation;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;

public class AsmNegTest extends AsmBase {
    @Test
    void negInstMem() {
        IntConst a = new IntConst(100);
        RegLocation ax = resolver.REG_AX.loc16();
        program.createNegInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", ax, resolver.saveInMem(a)),
                new AsmOp("neg", ax),
        });
    }

    @Test
    void negInstReg() {
        IntConst a = new IntConst(100);
        RegLocation ax = resolver.REG_AX.setContent(a);
        program.createNegInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("neg", ax),
        });
    }

}
