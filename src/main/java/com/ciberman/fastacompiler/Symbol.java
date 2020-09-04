package com.ciberman.fastacompiler;

import java.util.Objects;

public class Symbol {

    private final String name;

    private String type; // Change my later!

    public Symbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof String) {
            String string = (String) o;
            return name.equals(string);
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Symbol symbol = (Symbol) o;
        return name.equals(symbol.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
