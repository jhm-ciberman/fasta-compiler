package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.labels.Label;
import com.ciberman.fastacompiler.asm.mem.MemDeclaration;
import com.ciberman.fastacompiler.asm.mem.MemLocation;
import com.ciberman.fastacompiler.asm.program.AsmProgram;
import com.ciberman.fastacompiler.asm.reg.Reg;
import com.ciberman.fastacompiler.asm.reg.RegLocation;
import com.ciberman.fastacompiler.ir.*;
import org.jetbrains.annotations.NotNull;

public class AsmBuilder {
    private final AsmProgram asm;
    private final LocationResolver resolver;

    public AsmBuilder(LocationResolver resolver) {
        this.asm = new AsmProgram();
        this.resolver = resolver;
    }

    /**
     * Builds the AsmProgram.
     * @return The program
     */
    public AsmProgram build() {
        for (MemLocation location : this.resolver.getMemLocations()) {
            this.asm.addData(new MemDeclaration(location));
        }

        this.asm.addCode("ret");
        this.addErrorSection(Runtime.LABEL_DIVISION_BY_ZERO, Runtime.STR_ERROR_DIVISION_BY_ZERO);
        this.addErrorSection(Runtime.LABEL_OVERFLOW, Runtime.STR_ERROR_OVERFLOW);
        return this.asm;
    }

    private void addErrorSection(String labelName, StrConst errorString) {
        Label label = this.resolver.getLabel(labelName);
        if (label != null) {
            MemLocation location = this.resolver.memLocationOrNew(errorString);
            this.asm.addData(new MemDeclaration(location));
            this.asm.addLabel(label);
            this.asm.addPrint(location);
            this.asm.addCode("ret");
        }
    }

    /**
     * Inserts an add instruction.
     * @param instr The instruction
     * @param left The left operand location
     * @param right The right operand location
     */
    public void addAdd(AddInst instr, Location left, Location right) {
        this.asm.addCode("add", left, right);
        left.setContent(instr);
        this.asm.addJump("jo", this.getLabelOrNew(Runtime.LABEL_OVERFLOW));
    }

    /**
     * Finds a label or creates a new one
     * @param name The label name
     * @return The label
     */
    private Label getLabelOrNew(String name) {
        Label label = this.resolver.getLabel(name);
        if (label == null) {
            return this.resolver.addLabel(name);
        }
        return label;
    }

    /**
     * Inserts a sub instruction.
     * @param instr The instruction
     * @param left The left operand location
     * @param right The right operand location
     */
    public void addSub(SubInst instr, Location left, Location right) {
        this.asm.addCode("sub", left, right);
        left.setContent(instr);
        right.markAsFree();
    }

    /**
     * Inserts a mul instruction. The left operand MUST be in the register A.
     * @param instr The instruction
     * @param right The right operand location
     */
    public void addMul(MulInst instr, Location right) {
        this.spillRegIfNecessary(this.resolver.REG_DX);
        this.asm.addCode("mul", right);
        this.resolver.REG_AX.setContent(instr);
    }
    /**
     * Inserts a mul instruction. The left operand MUST be in the register A.
     * @param instr The instruction
     * @param right The right operand location
     */
    public void addDiv(DivInst instr, Location right) {
        this.asm.addCode("test", right, right);
        this.asm.addJump("jz", this.getLabelOrNew(Runtime.LABEL_DIVISION_BY_ZERO));
        if (right.getContent().getType() == ValueType.INT) {
            this.asm.addCode("cwd");
        } else {
            this.asm.addCode("cdq");
        }
        this.asm.addCode("div", right);
        this.resolver.REG_AX.setContent(instr);
        right.markAsFree();
    }

    /**
     * Inserts a mov instruction.
     * @param instr The instruction
     * @param left The left operand location
     * @param right The right operand location
     */
    public void addMov(AssignInst instr, MemLocation left, RegLocation right) {
        this.asm.addCode("mov", left, right);
        right.markAsFree();
        left.setContent(instr);
    }

    /**
     * Returns the mnemonic for the given relational operator
     * @param relOperator The relational operator
     * @return The mnemonic
     */
    private String getConditionMnemonic(BranchCondition.RelOperator relOperator) {
        switch (relOperator) {
            case GT:
                return "jg";
            case LT:
                return "jl";
            case GTE:
                return "jge";
            case LTE:
                return "jle";
            case EQ:
                return "je";
            case NOTEQ:
                return "jne";
        }
        return "jmp";
    }

    /**
     * Spill all volatile registers
     */
    private void preserveVolatileRegisters() {
        this.spillRegIfNecessary(this.resolver.REG_AX);
        this.spillRegIfNecessary(this.resolver.REG_CX);
        this.spillRegIfNecessary(this.resolver.REG_DX);
    }

    /**
     * Inserts a print integer instruction.
     * @param location The location of the integer param
     */
    public void addPrintInt(Location location) {
        this.preserveVolatileRegisters();
        this.moveToReg(location.getContent(), this.resolver.REG_AX);
        MemLocation format = this.resolver.memLocationOrNew(Runtime.STR_FORMAT_DECIMAL);
        this.asm.addCode("cwde");
        this.asm.addPrint(format, this.resolver.REG_AX.loc32());
        this.resolver.REG_AX.markAsFree();

    }

    /**
     * Inserts a print long instruction.
     * @param location The location of the long param
     */
    public void addPrintLong(Location location) {
        this.preserveVolatileRegisters();
        MemLocation format = this.resolver.memLocationOrNew(Runtime.STR_FORMAT_DECIMAL);
        this.asm.addPrint(format, location);
        location.markAsFree();
    }

    /**
     * Inserts a print string instruction.
     * @param location The location of the string to print
     */
    public void addPrintStr(MemLocation location) {
        this.preserveVolatileRegisters();
        this.asm.addPrint(location);
    }

    /**
     * Inserts a neg instruction.
     * @param instr The instruction
     * @param location The operand location
     */
    public void addNeg(NegInst instr, Location location) {
        this.asm.addCode("neg", location);
        location.setContent(instr);
    }

    /**
     * Inserts a target label in the code segment.
     * @param label The label to insert
     */
    public void addLabel(Label label) {
        this.asm.addLabel(label);
    }

    /**
     * Inserts a conditional jump
     * @param operator The relational operator
     * @param loc1 The left hand side register location
     * @param loc2 The right hand side register location
     * @param label The target jump label
     */
    public void addConditionalJump(@NotNull BranchCondition.RelOperator operator, RegLocation loc1, RegLocation loc2, Label label) {
        this.asm.addCode("cmp", loc1, loc2);
        loc1.markAsFree();
        loc2.markAsFree();
        String op = this.getConditionMnemonic(operator);
        this.asm.addJump(op, label);
    }

    /**
     * Inserts an unconditional jump.
     * @param label The target label
     */
    public void addUnconditionalJump(Label label) {
        this.asm.addJump("jmp", label);
    }

    /**
     * Inserts an itol instruction. The operand must be in the A register.
     * @param instr The instruction
     */
    public void addItol(ItolInst instr) {
        this.asm.addCode("cwde");
        this.resolver.REG_AX.setContent(instr);
    }

    /**
     * Resolves a value and moves it to a register if the value is in memory.
     * If the value is a 0, it will be created with a "xor".
     * @param value The value
     * @return The register location of the value
     */
    public @NotNull RegLocation moveToReg(Value value) {
        /*
        if (value instanceof IntConst) {
            int constantValue = ((IntConst) value).getValue();
            if (constantValue == 0) {
                RegLocation reg = this.requestReg(value);
                this.asm.addCode("xor",  reg, reg);
                return reg;
            }
        } else if (value instanceof LongConst) {
            long constantValue = ((LongConst) value).getValue();
            if (constantValue == 0) {
                RegLocation reg = this.requestReg(value);
                this.asm.addCode("xor",  reg, reg);
                return reg;
            }
        }*/

        Location loc = this.resolver.locationOrNew(value);
        if (loc instanceof RegLocation) {
            return (RegLocation) loc;
        }
        RegLocation reg = this.resolver.requestReg(value);
        this.asm.addCode("mov", reg, loc);
        return reg;
    }

    /**
     * Resolves a value and moves it to an specific target register if the value is in memory.
     * @param value The value
     * @param targetReg The target register
     */
    public void moveToReg(Value value, Reg targetReg) {
        Location loc = this.resolver.location(value);
        if (loc.isInReg(targetReg)) return;

        this.spillRegIfNecessary(targetReg);
        Location targetLoc = targetReg.getLocationFor(value);
        this.asm.addCode("mov", targetLoc, loc);
        targetReg.setContent(value);
        loc.markAsFree();
    }

    /**
     * Moves the specified register location to any other register
     * @param regLocation The register to move
     * @return The target register
     */
    public RegLocation moveToAnyOtherReg(RegLocation regLocation) {
        RegLocation reg = this.resolver.requestReg(regLocation.getContent());
        this.asm.addCode("mov", reg, regLocation);
        regLocation.markAsFree();
        return reg;
    }

    /**
     * Spill a register (Only if the register is in use)
     * @param reg The register to spill
     */
    public void spillRegIfNecessary(Reg reg) {
        Value content = reg.getContent();
        if (content == null) return;

        MemLocation memLocation = this.resolver.memLocationOrNew(content);
        RegLocation regLocation = reg.getCurrentLocation();
        this.asm.addCode("mov", memLocation, regLocation);
        assert regLocation != null;
        regLocation.markAsFree();
    }
}
