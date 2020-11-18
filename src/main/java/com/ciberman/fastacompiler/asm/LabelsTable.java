package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.ir.Inst;

import java.util.HashMap;
import java.util.Map;

public class LabelsTable {

    public static final String LABEL_OVERFLOW = "label_error_overflow";
    public static final String LABEL_DIVISION_BY_ZERO = "label_error_div_by_zero";

    private final Map<Inst, String> labelsMap = new HashMap<>();
    private int labelsCount = 0;
    public void addLabel(Inst target) {
        target.markAsLeader();
        this.labelsCount++;
        String name = "label_" + this.labelsCount;
        this.labelsMap.put(target, name);
    }

    public String get(Inst target) {
        return this.labelsMap.get(target);
    }


}
