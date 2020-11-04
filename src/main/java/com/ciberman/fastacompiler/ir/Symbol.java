package com.ciberman.fastacompiler.ir;

import com.ciberman.fastacompiler.out.IRValueStringConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Symbol implements Value {

    private final String name;

    private ValueType type; // Change my later!

    private boolean wasInitialized;

    private @Nullable Const initialValue;

    protected Symbol(String name, ValueType type) {
        this.name = name;
        this.type = type;
    }

    public boolean isInitialized() {
        return wasInitialized;
    }

    public void markAsInitialized() {
        this.wasInitialized = true;
    }

    public String getName() {
        return name;
    }

    public ValueType getType() {
        return this.type;
    }

    @Override
    public String toPrintableString(IRValueStringConverter valueVisitor) {
        return valueVisitor.getSymbolString(this);
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    @Override
    public String getDebugString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", initialValue=" + initialValue.getValueDebugString() +
                '}';
    }

    public void setInitialValue(@NotNull Const value) {
        this.initialValue = value;
        this.wasInitialized = true;
    }

    public @Nullable Const getInitialValue() {
        return initialValue;
    }
}
