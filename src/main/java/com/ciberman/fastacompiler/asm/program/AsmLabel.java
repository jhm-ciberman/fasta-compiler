package com.ciberman.fastacompiler.asm.program;

import com.ciberman.fastacompiler.asm.labels.Label;

import java.io.IOException;

public class AsmLabel implements AsmCode {

    public String label;

    public AsmLabel(Label label) {
        this.label = label.getLabel();
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
