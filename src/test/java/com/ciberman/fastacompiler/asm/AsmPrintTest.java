package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmOp;
import com.ciberman.fastacompiler.asm.program.AsmPrint;
import com.ciberman.fastacompiler.ir.IntConst;
import com.ciberman.fastacompiler.ir.LongConst;
import com.ciberman.fastacompiler.ir.StrConst;
import org.junit.jupiter.api.Test;

public class AsmPrintTest extends AsmBase {
    @Test
    void printInstStr() {
        StrConst a = new StrConst("Hello World");

        program.createPrintInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmPrint(resolver.saveInMem(a)),
                new AsmOp("ret"),
        });
    }

    @Test
    void printInstLong() {
        LongConst a = new LongConst(5);

        program.createPrintInst(a);

        MemLocation format = resolver.memLocationOrNew(Runtime.STR_FORMAT_DECIMAL);
        this.assertAsmEqual(new AsmCode[] {
                new AsmPrint(format, resolver.saveInMem(a)),
                new AsmOp("ret"),
        });
    }

    @Test
    void printInstInt() {
        IntConst a = new IntConst(5);
        MemLocation format = resolver.memLocationOrNew(Runtime.STR_FORMAT_DECIMAL);

        program.createPrintInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.saveInMem(a)),
                new AsmOp("cwde"),
                new AsmPrint(format, resolver.REG_AX.loc32()),
                new AsmOp("ret"),
        });
    }
}
