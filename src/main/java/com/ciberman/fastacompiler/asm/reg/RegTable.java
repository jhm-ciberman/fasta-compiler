package com.ciberman.fastacompiler.asm.reg;

import com.ciberman.fastacompiler.ir.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RegTable {
    private final Map<Value, RegLocation> valueLocMap = new HashMap<>();
    private final Map<RegLocation, Value> locValueMap = new HashMap<>();

    private final Queue<Reg> emptyRegs = new ArrayDeque<>();
    private final List<Reg> registers = new ArrayList<>();

    public Reg createReg(String name32, String name16) {
        Reg location = new Reg(this, name32, name16);
        this.emptyRegs.add(location);
        this.registers.add(location);
        return location;
    }

    public @Nullable RegLocation requestReg(Value value) {
        Reg reg = this.emptyRegs.poll();
        if (reg != null) {
            return reg.setContent(value);
        }
        return null;
    }

    public @Nullable RegLocation findReg(Value value) {
        RegLocation regLocation = this.valueLocMap.get(value);
        if (regLocation != null) {
            this.emptyRegs.remove(regLocation.getReg());
        }
        return regLocation;
    }

    public @Nullable Value getValue(@NotNull RegLocation regLocation) {
        return this.locValueMap.get(regLocation);
    }

    public void clearRegContent(@NotNull RegLocation regLocation) {
        Value oldValue = this.getValue(regLocation);
        if (oldValue != null) {
            this.valueLocMap.remove(oldValue);
            this.locValueMap.remove(regLocation);
            this.emptyRegs.add(regLocation.getReg());
        }
    }

    public void updateRegContent(@NotNull RegLocation regLocation, @NotNull Value value) {
        this.clearRegContent(regLocation);

        this.valueLocMap.put(value, regLocation);
        this.locValueMap.put(regLocation, value);

        this.emptyRegs.remove(regLocation.getReg());
    }

    public void printRegistersContents() {
        for (Reg reg : this.registers) {
            System.out.println(reg.getName() + " = " + reg.getContent());
        }
    }
}