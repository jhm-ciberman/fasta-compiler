package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.program.AsmCode;
import com.ciberman.fastacompiler.asm.program.AsmProgram;
import com.ciberman.fastacompiler.ir.IRProgram;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class AsmBase {

    protected AsmGenerator generator;
    protected LocationResolver resolver;
    protected IRProgram program;

    @BeforeEach
    void setUp() {
        this.generator = new AsmGenerator();
        this.resolver = generator.getResolver();
        this.program = new IRProgram();
    }

    protected void assertAsmEqual(AsmCode[] expectedArr) {
        AsmProgram asm = this.generator.generate(this.program);
        StringBuilder actualStr = new StringBuilder();
        for (AsmCode op : asm.getCodeSegment()) {
            actualStr.append(op.toString()).append("\n");
        }
        StringBuilder expectedStr = new StringBuilder();
        for (AsmCode op : expectedArr) {
            expectedStr.append(op.toString()).append("\n");
        }

        Assertions.assertEquals(expectedStr.toString(), actualStr.toString());
    }
}
