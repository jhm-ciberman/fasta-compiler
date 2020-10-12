package com.ciberman.fastacompiler.ir;

public interface Value {

    enum Type {
        LONG, INT, STR,
    }


    Type getType();
}
