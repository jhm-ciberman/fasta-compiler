package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.asm.reg.RegLocation;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;

public class AsmItolTest extends AsmBase {

    @Test
    void itolInstRegA() {
        IntConst a = new IntConst(100);
        resolver.REG_AX.setContent(a);

        program.createItolInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("cwde"),
        });
    }

    @Test
    void itolInstRegNotA() {
        IntConst a = new IntConst(100);
        RegLocation bx = resolver.REG_BX.setContent(a);

        program.createItolInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), bx),
                new AsmOp("cwde"),
        });
    }

    @Test
    void itolInstMem() {
        IntConst a = new IntConst(100);
        MemLocation memLocation = resolver.saveInMem(a);

        program.createItolInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), memLocation),
                new AsmOp("cwde"),
        });
    }

}
