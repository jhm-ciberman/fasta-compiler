package com.ciberman.fastacompiler.asm.program;

import java.io.IOException;

public class AsmComment implements AsmCode {

    private final String text;

    public AsmComment(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void accept(AsmVisitor visitor) throws IOException {
        visitor.comment(this);
    }
}
