package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.asm.mem.MemTable;
import com.ciberman.fastacompiler.asm.reg.Reg;
import com.ciberman.fastacompiler.asm.reg.RegLocation;
import com.ciberman.fastacompiler.asm.reg.RegTable;
import com.ciberman.fastacompiler.ir.Value;
import org.jetbrains.annotations.NotNull;

public class LocationResolver {

    private final RegTable reg;
    private final MemTable mem;

    public final Reg REG_AX;
    public final Reg REG_BX;
    public final Reg REG_CX;
    public final Reg REG_DX;

    public LocationResolver() {
        this.mem = new MemTable();
        this.reg = new RegTable();

        this.REG_AX = this.reg.createReg("eax", "ax");
        this.REG_BX = this.reg.createReg("ebx", "bx");
        this.REG_CX = this.reg.createReg("ecx", "cx");
        this.REG_DX = this.reg.createReg("edx", "dx");
    }

    /**
     * Resolves the location (in memory or register) of the given value or fails
     * @param value The value
     * @return The resolved location
     */
    public @NotNull Location location(Value value) {
        Location loc = this.reg.findReg(value);
        if (loc != null) return loc;
        Location memLoc = this.mem.find(value);
        if (memLoc == null) {
            throw new RuntimeException("Value " + value + " is not declared");
        }
        return memLoc;
    }

    /**
     * Resolves the location of the given value (in memory or register).
     * If is not found, a new location in memory is created to hold that value.
     * @param value The value
     * @return The location
     */
    public @NotNull Location locationOrNew(Value value) {
        RegLocation loc = this.reg.findReg(value);
        if (loc != null) return loc;
        return this.memLocation(value);
    }

    /**
     * Find the location in memory of the given value. If is not found a new
     * value in memory will be created.
     * @param value The value
     * @return The memory location
     */
    public @NotNull MemLocation memLocation(Value value) {
        MemLocation memLocation = this.mem.find(value);
        if (memLocation == null) {
            return this.mem.save(value);
        }
        return memLocation;
    }

    /**
     * Request a register to hold the given value.
     * If no register is available, an exception is thrown.
     * @param value The value
     * @return The register location
     */
    public @NotNull RegLocation requestReg(Value value) {
        RegLocation reg = this.reg.requestReg(value);
        if (reg == null) {
            this.reg.printRegistersContents();
            throw new RuntimeException("Not enough registers.");
        }
        reg.setContent(value);
        return reg;
    }

    /**
     * Reserves a memory location for the given symbol
     * @param symbol The symbol to save
     * @return The asigned memory location for the symbol
     */
    public MemLocation saveInMem(Value symbol) {
        return this.mem.save(symbol);
    }

    /**
     * @return All the registered memory locations
     */
    public Iterable<MemLocation> getMemLocations() {
        return this.mem.getLocations();
    }
}
