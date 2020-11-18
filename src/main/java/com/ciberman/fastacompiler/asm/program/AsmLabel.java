package com.ciberman.fastacompiler.asm.program;

import java.io.IOException;

public class AsmLabel implements AsmCode {

    public String label;

    public AsmLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.label + ":";
    }

    @Override
    public void accept(AsmVisitor visitor) throws IOException {
        visitor.label(this);
    }
}
