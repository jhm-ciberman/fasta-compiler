package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.labels.Label;
import com.ciberman.fastacompiler.asm.labels.LabelsTable;
import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.asm.mem.MemTable;
import com.ciberman.fastacompiler.asm.reg.Reg;
import com.ciberman.fastacompiler.asm.reg.RegLocation;
import com.ciberman.fastacompiler.asm.reg.RegTable;
import com.ciberman.fastacompiler.ir.Inst;
import com.ciberman.fastacompiler.ir.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationResolver {

    private final RegTable reg;
    private final MemTable mem;
    private final LabelsTable labels;

    public final Reg REG_AX;
    public final Reg REG_BX;
    public final Reg REG_CX;
    public final Reg REG_DX;

    public LocationResolver() {
        this.mem = new MemTable();
        this.reg = new RegTable();
        this.labels = new LabelsTable();

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
        return this.memLocationOrNew(value);
    }

    /**
     * Find the location in memory of the given value. If is not found a new
     * value in memory will be created.
     * @param value The value
     * @return The memory location
     */
    public @NotNull MemLocation memLocationOrNew(Value value) {
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
     * @return The assigned memory location for the symbol
     */
    public MemLocation saveInMem(Value symbol) {
        return this.mem.save(symbol);
    }

    /**
     * Reserves a memory location for the given value
     * @param value The value to find
     * @return The assigned memory location for the value
     */
    public MemLocation memLocation(Value value) {
        return this.mem.find(value);
    }

    /**
     * @return All the registered memory locations
     */
    public Iterable<MemLocation> getMemLocations() {
        return this.mem.getLocations();
    }

    /**
     * Returns the label associated with that target
     * @param instr The target
     * @return The associated label
     */
    public @Nullable Label getLabel(Inst instr) {
        return this.labels.get(instr);
    }

    /**
     * Returns the named label associated with that name
     * @param name The label name
     * @return The associated label
     */
    public @Nullable Label getLabel(String name) {
        return this.labels.getNamedLabel(name);
    }

    /**
     * Adds a label for the specified target
     * @param target The target
     */
    public @NotNull Label addLabel(Inst target) {
        return this.labels.addLabel(target);
    }

    /**
     * Adds a named label
     * @param name The label name
     * @return The added label
     */
    public Label addLabel(String name) {
        return this.labels.addNamedLabel(name);
    }
}
