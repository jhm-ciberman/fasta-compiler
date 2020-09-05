package com.ciberman.fastacompiler;

public class Symbol {

    public enum Type {
        UNKNOWN, INT, LONG, STRING,
    }

    private final String name;

    private Type type; // Change my later!

    public Symbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
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
