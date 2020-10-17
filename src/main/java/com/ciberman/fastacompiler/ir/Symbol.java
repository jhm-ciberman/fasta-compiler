package com.ciberman.fastacompiler.ir;

public class Symbol {

    private final String name;

    private ValueType type; // Change my later!

    private Inst lastAssignment;

    protected Symbol(String name, ValueType type) {
        this.name = name;
        this.type = type;
    }

    public Inst getLastAssignment() {
        return lastAssignment;
    }

    public void setLastAssignment(Inst inst) {
        this.lastAssignment = inst;
    }

    public String getName() {
        return name;
    }

    public ValueType getType() {
        return this.type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}