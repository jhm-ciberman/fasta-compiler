package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Value;
import com.ciberman.fastacompiler.ir.ValueType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class RegLocation implements Location {
    private final RegTable table;
    private final String name32;
    private final String name16;
    private @Nullable Value content;

    public RegLocation(RegTable table, String name32, String name16) {
        this.table = table;
        this.name32 = name32;
        this.name16 = name16;
        this.content = null;
    }

    @Override
    public String getName() {
        if (this.content != null && this.content.getType() == ValueType.INT) {
            return this.name16;
        }
        return this.name32;
    }

    @Override
    public boolean isReg() {
        return true;
    }

    @Override
    public void setContent(@NotNull Value content) {
        this.table.updateRegContent(this, this.content, content);
        this.content = content;
    }

    public void markAsFree() {
        this.table.updateRegContent(this, this.content, null);
        this.content = null;
    }

    public @Nullable Value getContent() {
        return this.content;
    }

    public String toString() {
        return this.getName();
    }
}