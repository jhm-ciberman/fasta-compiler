package com.ciberman.fastacompiler.asm.program;

import com.ciberman.fastacompiler.asm.Location;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class AsmOp implements AsmCode {
    private final String type;
    private final @Nullable Location loc1;
    private final @Nullable Location loc2;

    public AsmOp(String type, @Nullable Location loc1, @Nullable Location loc2) {
        this.type = type;
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public AsmOp(String type, @Nullable Location loc1) {
        this(type, loc1, null);
    }


    public AsmOp(String type) {
        this(type, null, null);
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        String str = type;
        if (loc1 != null) {
            str += " " + loc1.getName();
        }
        if (loc2 != null) {
            str += ", " + loc2.getName();
        }
        return str;
    }

    public @Nullable Location getLoc1() {
        return loc1;
    }

    public @Nullable Location getLoc2() {
        return loc2;
    }

    @Override
    public void accept(AsmVisitor visitor) throws IOException {
        visitor.op(this);
    }
}
