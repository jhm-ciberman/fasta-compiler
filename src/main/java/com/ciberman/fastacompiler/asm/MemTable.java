package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Const;
import com.ciberman.fastacompiler.ir.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class MemTable {

    private final HashMap<Value, MemLocation> constNames = new HashMap<>();
    private int idCount = 0;

    public @NotNull MemLocation findLocationOrCreate(Value value) {
        MemLocation location = this.findLocation(value);
        if (location != null) return location;

        if (value instanceof Const) {
            location = new ConstMemLocation(this, "@" + this.idCount, value);
        } else {
            location = new DataMemLocation(this, "@" + this.idCount, value);
        }
        this.idCount++;
        this.constNames.put(value, location);
        return location;
    }

    public @Nullable MemLocation findLocation(Value value) {
        return this.constNames.get(value);
    }

    public String getDebugDeclaredLocations() {
        StringBuilder str = new StringBuilder();
        for (MemLocation loc : this.getGlobalLocations()) {
            str.append(loc.toString()).append(" ");
        }
        return str.toString();
    }

    public void updateLocation(Value value, MemLocation location) {
        this.constNames.put(value, location);
    }

    public Iterable<MemLocation> getGlobalLocations() {
        return this.constNames.values();
    }
}
