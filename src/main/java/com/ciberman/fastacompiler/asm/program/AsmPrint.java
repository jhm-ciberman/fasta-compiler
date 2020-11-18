package com.ciberman.fastacompiler.asm.program;

import com.ciberman.fastacompiler.asm.Location;
import com.ciberman.fastacompiler.asm.mem.MemLocation;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class AsmPrint implements AsmCode {

    private final MemLocation format;

    private @Nullable final Location arg;

    public AsmPrint(MemLocation format, @Nullable Location arg) {
        this.format = format;
        this.arg = arg;
    }

    public AsmPrint(MemLocation format) {
        this.format = format;
        this.arg = null;
    }

    public MemLocation getFormat() {
        return format;
    }

    public @Nullable Location getArg() {
        return arg;
    }

    @Override
    public String toString() {
        String str = "print " + this.format.toString();
        if (this.arg != null) {
            return str + ", " + this.arg.toString();
        }
        return str;
    }

    @Override
    public void accept(AsmVisitor visitor) throws IOException {
        visitor.print(this);
    }
}
