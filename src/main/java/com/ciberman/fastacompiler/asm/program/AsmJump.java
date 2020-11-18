package com.ciberman.fastacompiler.asm.program;

import java.io.IOException;

public class AsmJump implements AsmCode {

    private final String jumpType;
    private final String label;

    public AsmJump(String jumpType, String label) {
        this.jumpType = jumpType;
        this.label = label;
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
