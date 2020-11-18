package com.ciberman.fastacompiler.asm.mem;

import com.ciberman.fastacompiler.asm.ConstMemLocation;
import com.ciberman.fastacompiler.asm.DataMemLocation;
import com.ciberman.fastacompiler.ir.*;
import com.ciberman.fastacompiler.out.IRValueStringConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class MemTable {

    static class NameResolver implements IRValueStringConverter {
        private int idCount = 0;
        @Override
        public String getIntConstString(IntConst c) {
            return c.getValueDebugString();
        }

        @Override
        public String getLongConstString(LongConst c) {
            return c.getValueDebugString();
        }

        @Override
        public String getStrConstString(StrConst c) {
            this.idCount++;
            return "str_" + this.idCount;
        }

        @Override
        public String getInstrString(Inst instr) {
            this.idCount++;
            return "inst_" + this.idCount;
        }

        @Override
        public String getSymbolString(Symbol symbol) {
            return symbol.getName().replace(".", "@");
        }
    }

    private final HashMap<Value, MemLocation> constNames = new HashMap<>();
    private final NameResolver nameResolver = new NameResolver();

    public @NotNull MemLocation save(Value value) {
        if (this.constNames.containsKey(value)) {
            throw new RuntimeException("Cannot save the value " + value.getDebugString() + " twice.");
        }
        String name = "@" + value.toPrintableString(this.nameResolver);
        MemLocation location;
        if (value instanceof Const) {
            location = new ConstMemLocation(this, name, value);
        } else {
            location = new DataMemLocation(this, name, value);
        }
        this.constNames.put(value, location);
        return location;
    }

    public @Nullable MemLocation find(Value value) {
        return this.constNames.get(value);
    }

    public String getDebugDeclaredLocations() {
        StringBuilder str = new StringBuilder();
        for (MemLocation loc : this.getLocations()) {
            str.append(loc.toString()).append(" ");
        }
        return str.toString();
    }

    public void update(Value value, MemLocation location) {
        this.constNames.replace(value, location);
    }

    public Iterable<MemLocation> getLocations() {
        return this.constNames.values();
    }
}
