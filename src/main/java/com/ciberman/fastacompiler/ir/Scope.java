package com.ciberman.fastacompiler.ir;

public class Scope {
    private final String name;

    private final Scope parent;

    public Scope(String name, Scope parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        if (this.parent == null) {
            return this.name;
        }
        return this.parent.getName() + "." + this.name;
    }

    public String getName(String name) {
        return this.getName() + "." + name;
    }

    public Scope getParent() {
        return parent;
    }
}
