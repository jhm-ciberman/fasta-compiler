package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Value;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class MemTable {

    private final HashMap<Value, MemLocation> constNames = new HashMap<>();
    private int idCount = 0;

    public MemLocation reserveGlobalLocation(Value value) {
        MemLocation location = new MemLocation(this, "@" + this.idCount);
        this.constNames.put(value, location);
        this.idCount++;
        return location;
    }

    public @Nullable MemLocation findLocation(Value value) {
        return this.constNames.get(value);
    }

    public void updateLocation(Value value, MemLocation location) {
        this.constNames.put(value, location);
    }
}
