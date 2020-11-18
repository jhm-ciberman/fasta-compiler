package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.labels.Label;
import com.ciberman.fastacompiler.asm.program.*;
import com.ciberman.fastacompiler.ir.IntConst;
import org.junit.jupiter.api.Test;

public class AsmAddTest extends AsmBase {
    @Test
    void addInstMemMem() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createAddInst(a, b);

        Label label = new Label(Runtime.LABEL_OVERFLOW);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.saveInMem(b)),
                new AsmOp("add", resolver.REG_AX.loc16(), resolver.saveInMem(a)),
                new AsmJump("jo", label),
                new AsmOp("ret"),
                new AsmLabel(label),
                new AsmPrint(resolver.memLocationOrNew(Runtime.STR_ERROR_OVERFLOW)),
                new AsmOp("ret"),
        });
    }

    @Test
    void addInstRegMem() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createAddInst(a, b);

        resolver.REG_AX.setContent(a);
        Label label = new Label(Runtime.LABEL_OVERFLOW);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("add", resolver.REG_AX.loc16(), resolver.saveInMem(b)),
                new AsmJump("jo", label),
                new AsmOp("ret"),
                new AsmLabel(label),
                new AsmPrint(resolver.memLocationOrNew(Runtime.STR_ERROR_OVERFLOW)),
                new AsmOp("ret"),
        });
    }

    @Test
    void addInstMemReg() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createAddInst(a, b);

        resolver.REG_AX.setContent(b);
        Label label = new Label(Runtime.LABEL_OVERFLOW);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("add", resolver.REG_AX.loc16(), resolver.saveInMem(a)),
                new AsmJump("jo", label),
                new AsmOp("ret"),
                new AsmLabel(label),
                new AsmPrint(resolver.memLocationOrNew(Runtime.STR_ERROR_OVERFLOW)),
                new AsmOp("ret"),
        });
    }

    @Test
    void addInstRegReg() {
        IntConst a = new IntConst(2);
        IntConst b = new IntConst(100);

        program.createAddInst(a, b);

        resolver.REG_AX.setContent(a);
        resolver.REG_BX.setContent(b);

        Label label = new Label(Runtime.LABEL_OVERFLOW);
        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("add", resolver.REG_AX.loc16(), resolver.REG_BX.loc16()),
                new AsmJump("jo", label),
                new AsmOp("ret"),
                new AsmLabel(label),
                new AsmPrint(resolver.memLocationOrNew(Runtime.STR_ERROR_OVERFLOW)),
                new AsmOp("ret"),
        });
    }
}
