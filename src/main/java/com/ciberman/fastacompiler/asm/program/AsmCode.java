package com.ciberman.fastacompiler.asm.program;

import java.io.IOException;

public interface AsmCode {
    void accept(AsmVisitor visitor) throws IOException;
}
