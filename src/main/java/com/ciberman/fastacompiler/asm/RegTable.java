package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class RegTable {
    private final Map<Value, RegLocation> valueRegMap = new HashMap<>();
    private final Queue<RegLocation> emptyRegLocations = new ArrayDeque<>();

    public RegLocation createReg(String name32, String name16) {
        RegLocation location = new RegLocation(this, name32, name16);
        this.emptyRegLocations.add(location);
        return location;
    }

    public @Nullable RegLocation requestReg(Value value) {
        RegLocation reg = this.emptyRegLocations.poll();
        if (reg != null) {
            reg.setContent(value);
        }
        return reg;
    }

    public @Nullable RegLocation findReg(Value value) {
        RegLocation regLocation = this.valueRegMap.get(value);
        this.emptyRegLocations.remove(regLocation);
        return regLocation;
    }

    public void updateRegContent(@NotNull RegLocation regLocation, @Nullable Value oldValue, @Nullable Value value) {

        if (oldValue != null) {
            this.valueRegMap.remove(oldValue);
        }

        if (value != null) {
            this.valueRegMap.put(value, regLocation);
            this.emptyRegLocations.remove(regLocation);
        } else {
            this.valueRegMap.remove(value, regLocation);
            this.emptyRegLocations.add(regLocation);
        }
    }

    public Iterable<RegLocation> getAvaiableRegisters() {
        return this.emptyRegLocations;
    }
}