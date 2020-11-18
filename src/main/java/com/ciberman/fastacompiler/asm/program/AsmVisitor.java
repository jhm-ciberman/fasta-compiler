package com.ciberman.fastacompiler.asm.program;

import java.io.IOException;

public interface AsmVisitor {
    void op(AsmOp asmOp) throws IOException;

    void comment(AsmComment asmOp) throws IOException;

    void label(AsmLabel asmOp) throws IOException;

    void jump(AsmJump asmJump) throws IOException;

    void print(AsmPrint asmPrint) throws IOException;
}
