package com.ciberman.fastacompiler.asm.mem;

import com.ciberman.fastacompiler.ir.*;
import com.ciberman.fastacompiler.out.IRValueStringConverter;

public class MemDeclaration implements IRValueStringConverter {

    private final MemLocation location;

    public MemDeclaration(MemLocation location) {
        this.location = location;
    }

    public String getName() {
        return this.location.getName();
    }

    @Override
    public String toString() {
        String str = this.location.getValue().toPrintableString(this);
        return location.getName() + " " + str + "        ; " + this.location.getValue().toString();
    }

    @Override
    public String getIntConstString(IntConst c) {
        return "dw " + c.getValue();
    }

    @Override
    public String getLongConstString(LongConst c) {
        return "dd " + c.getValue();
    }

    @Override
    public String getStrConstString(StrConst c) {
        return "db " + this.getStringConstValue(c);
    }

    protected String getStringConstValue(StrConst strConst) {
        String str = strConst.getValue();
        if (str.isEmpty()) return "0";

        str = str.replace("'", "\\'")
                .replaceAll("\\\\n", "', 10, '")
                .replace("\\", "\\\\");

        str = "'" + str + "', 0";
        str = str.replaceAll("'', ", "");
        return str;
    }

    @Override
    public String getInstrString(Inst instr) {
        if (instr instanceof Value) {
            Value value = (Value) instr;
            return value.getType() == ValueType.INT ? "dw ?" : "dd ?";
        }
        return "dw ?"; // This should never happen
    }

    @Override
    public String getSymbolString(Symbol symbol) {
        ValueType type = symbol.getType();
        Value initialValue = symbol.getInitialValue();
        if (initialValue == null) {
            return (type == ValueType.INT) ? "dw ?" : "dd ?";
        } else {
            return (type == ValueType.INT)
                    ? "dw " + ((IntConst) initialValue).getValue()
                    : "dd " + ((LongConst) initialValue).getValue();
        }
    }
}
