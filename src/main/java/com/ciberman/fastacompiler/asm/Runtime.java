package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.StrConst;

public class Runtime {
    public static final String LABEL_OVERFLOW = "label_error_overflow";
    public static final String LABEL_DIVISION_BY_ZERO = "label_error_div_by_zero";

    public static final StrConst STR_FORMAT_DECIMAL = new StrConst("%d");
    public static final StrConst STR_ERROR_OVERFLOW = new StrConst("ERROR: Overflow in addition.\\n");
    public static final StrConst STR_ERROR_DIVISION_BY_ZERO = new StrConst("ERROR: Division by zero.\\n");
}
