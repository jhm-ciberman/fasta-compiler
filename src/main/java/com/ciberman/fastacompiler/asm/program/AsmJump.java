package com.ciberman.fastacompiler.asm.program;

import com.ciberman.fastacompiler.asm.labels.Label;

import java.io.IOException;

public class AsmJump implements AsmCode {

    private final String jumpType;
    private final String label;

    public AsmJump(String jumpType, Label label) {
        this.jumpType = jumpType;
        this.label = label.getLabel();
    }

    public String getJumpType() {
        return jumpType;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.jumpType + " " + this.label;
    }

    @Override
    public void accept(AsmVisitor visitor) throws IOException {
        visitor.jump(this);
    }
}
