package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Value;

public interface Location {
    String getName();
    boolean isReg();
    void setContent(Value content);
    void markAsFree();
}
