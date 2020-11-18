package com.ciberman.fastacompiler.asm.reg;

import com.ciberman.fastacompiler.asm.Location;
import com.ciberman.fastacompiler.ir.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegLocation implements Location {
    private final Reg reg;
    private final String name;


    public RegLocation(Reg reg, String name) {
        this.reg = reg;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isReg() {
        return true;
    }

    @Override
    public void setContent(@NotNull Value content) {
        this.reg.setContent(content);
    }

    public void markAsFree() {
        this.reg.markAsFree();
    }

    @Override
    public boolean isInReg(Reg reg) {
        return (this.reg == reg);
    }

    public @Nullable Value getContent() {
        return this.reg.getContent();
    }

    public String toString() {
        return this.getName();
    }

    public Reg getReg() {
        return this.reg;
    }
}