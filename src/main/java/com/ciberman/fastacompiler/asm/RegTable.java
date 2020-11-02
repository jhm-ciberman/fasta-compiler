package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Value;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class RegTable {
    private final Map<Value, RegLocation> valueRegMap = new HashMap<>();
    private final Queue<RegLocation> emptyRegLocations = new ArrayDeque<>();

    public final RegLocation REG_AX = new RegLocation(this,  "eax", "ax");
    public final RegLocation REG_BX = new RegLocation(this,  "ebx", "bx");
    public final RegLocation REG_CX = new RegLocation(this,  "ecx", "cx");
    public final RegLocation REG_DX = new RegLocation(this,  "edx", "dx");

    public RegTable() {
        this.emptyRegLocations.add(this.REG_AX);
        this.emptyRegLocations.add(this.REG_BX);
        this.emptyRegLocations.add(this.REG_CX);
        this.emptyRegLocations.add(this.REG_DX);
    }

    public @Nullable RegLocation requestReg(Value value) {
        RegLocation reg = this.emptyRegLocations.poll();
        if (reg != null) {
            reg.setContent(value);
        }
        return reg;
    }

    public void freeReg(RegLocation regLocation) {
        this.valueRegMap.remove(regLocation.getContent());
    }

    public @Nullable RegLocation findLocation(Value value) {
        return this.valueRegMap.get(value);
    }

    public void updateLocation(RegLocation regLocation, Value value) {
        this.valueRegMap.put(value, regLocation);
    }
}