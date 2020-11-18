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
        });
    }

    @Test
    void printInstLong() {
        LongConst a = new LongConst(5);
        MemLocation format = generator.getBuilder().getFormatDecimalMemLocation();

        program.createPrintInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmPrint(format, resolver.saveInMem(a)),
        });
    }

    @Test
    void printInstInt() {
        IntConst a = new IntConst(5);
        MemLocation format = generator.getBuilder().getFormatDecimalMemLocation();

        program.createPrintInst(a);

        this.assertAsmEqual(new AsmCode[] {
                new AsmOp("mov", resolver.REG_AX.loc16(), resolver.saveInMem(a)),
                new AsmOp("cwde"),
                new AsmPrint(format, resolver.REG_AX.loc32()),
        });
    }
}
