package com.ciberman.fastacompiler.ir;

public enum ValueType {
    LONG, INT, STR;

    public String toEnglish() {
        switch (this) {
            case LONG: return "a LONG";
            case INT:  return "an INT";
            case STR:  return "a STR";
            default:   return this.toString();
        }
    }
}
