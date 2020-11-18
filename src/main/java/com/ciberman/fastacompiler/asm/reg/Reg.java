package com.ciberman.fastacompiler.asm.reg;

import com.ciberman.fastacompiler.ir.Value;
import com.ciberman.fastacompiler.ir.ValueType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Reg {

    private final RegTable table;
    private final RegLocation loc32;
    private final RegLocation loc16;
    private @Nullable RegLocation currentLocation;

    public Reg(RegTable table, String name32, String name16) {
        this.table = table;
        this.loc32 = new RegLocation(this, name32);
        this.loc16 = new RegLocation(this, name16);
        this.currentLocation = null;
    }

    public @NotNull RegLocation loc32() {
        return this.loc32;
    }
    public @NotNull RegLocation loc16() {
        return this.loc16;
    }

    public String getName() {
        return this.loc32.getName();
    }

    public @Nullable Value getContent() {
        return this.currentLocation == null ? null : this.table.getValue(this.currentLocation);
    }

    public RegLocation getLocationFor(@NotNull Value value) {
        return (value.getType() == ValueType.INT) ? this.loc16 : this.loc32;
    }

    public RegLocation setContent(@NotNull Value value) {
        RegLocation location = this.getLocationFor(value);
        this.table.updateRegContent(location, value);
        this.currentLocation = location;
        return location;
    }

    public void markAsFree() {
        if (this.currentLocation != null) {
            this.table.clearRegContent(this.currentLocation);
            this.currentLocation = null;
        }
    }

    public @Nullable RegLocation getCurrentLocation() {
        return this.currentLocation;
    }
}
