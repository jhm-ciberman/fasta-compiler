package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.labels.Label;
import com.ciberman.fastacompiler.asm.program.*;
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

        Label label = new Label(Runtime.LABEL_DIVISION_BY_ZERO);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", ax, resolver.saveInMem(a)),
                new AsmOp("mov", bx, resolver.saveInMem(b)),
                new AsmOp("test", bx, bx),
                new AsmJump("jz", label),
                new AsmOp("cwd"),
                new AsmOp("div", bx),
                new AsmOp("ret"),
                new AsmLabel(label),
                new AsmPrint(resolver.memLocationOrNew(Runtime.STR_ERROR_DIVISION_BY_ZERO)),
                new AsmOp("ret"),
        });
    }

    @Test
    void divInstRegAMem() {
        IntConst a = new IntConst(100);
        IntConst b = new IntConst(2);

        RegLocation ax = resolver.REG_AX.loc16();
        RegLocation bx = resolver.REG_BX.loc16();

        program.createDivInst(a, b);
        resolver.REG_AX.setContent(a);

        Label label = new Label(Runtime.LABEL_DIVISION_BY_ZERO);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", bx, resolver.saveInMem(b)),
                new AsmOp("test", bx, bx),
                new AsmJump("jz", label),
                new AsmOp("cwd"),
                new AsmOp("div", bx),
                new AsmOp("ret"),
                new AsmLabel(label),
                new AsmPrint(resolver.memLocationOrNew(Runtime.STR_ERROR_DIVISION_BY_ZERO)),
                new AsmOp("ret"),
        });
    }

    @Test
    void divInstRegBMem() {
        IntConst a = new IntConst(100);
        IntConst b = new IntConst(2);

        RegLocation ax = resolver.REG_AX.loc16();
        RegLocation bx = resolver.REG_BX.loc16();
        RegLocation cx = resolver.REG_CX.loc16();

        program.createDivInst(a, b);
        resolver.REG_BX.setContent(a);

        Label label = new Label(Runtime.LABEL_DIVISION_BY_ZERO);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", ax, bx),
                new AsmOp("mov", cx, resolver.saveInMem(b)),
                new AsmOp("test", cx, cx),
                new AsmJump("jz", label),
                new AsmOp("cwd"),
                new AsmOp("div", cx),
                new AsmOp("ret"),
                new AsmLabel(label),
                new AsmPrint(resolver.memLocationOrNew(Runtime.STR_ERROR_DIVISION_BY_ZERO)),
                new AsmOp("ret"),
        });
    }

    @Test
    void divInstRegBRegC() {
        IntConst a = new IntConst(100);
        IntConst b = new IntConst(2);

        RegLocation ax = resolver.REG_AX.loc16();
        RegLocation bx = resolver.REG_BX.loc16();
        RegLocation cx = resolver.REG_CX.loc16();

        program.createDivInst(a, b);
        resolver.REG_BX.setContent(a);
        resolver.REG_CX.setContent(b);

        Label label = new Label(Runtime.LABEL_DIVISION_BY_ZERO);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", ax, bx),
                new AsmOp("test", cx, cx),
                new AsmJump("jz", label),
                new AsmOp("cwd"),
                new AsmOp("div", cx),
                new AsmOp("ret"),
                new AsmLabel(label),
                new AsmPrint(resolver.memLocationOrNew(Runtime.STR_ERROR_DIVISION_BY_ZERO)),
                new AsmOp("ret"),
        });
    }
}
