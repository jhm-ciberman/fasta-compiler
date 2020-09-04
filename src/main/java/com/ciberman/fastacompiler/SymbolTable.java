package com.ciberman.fastacompiler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private final Map<String, Symbol> symbolMap = new HashMap<>();

    public void add(Symbol symbol) {
        this.symbolMap.put(symbol.getName(), symbol);
    }

    public Symbol get(String name) {
        return this.symbolMap.get(name);
    }

    public Collection<Symbol> all() {
        return this.symbolMap.values();
    }
}
