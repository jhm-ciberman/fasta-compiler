package com.ciberman.fastacompiler.asm.labels;

import com.ciberman.fastacompiler.ir.Inst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LabelsTable {
    private final Map<Inst, Label> labelsMap = new HashMap<>();
    private final Map<String, Label> namedLabels = new HashMap<>();

    private int labelsCount = 0;

    public @NotNull Label addLabel(Inst target) {
        target.markAsLeader();
        this.labelsCount++;
        Label label = new Label("label_" + this.labelsCount);
        this.labelsMap.put(target, label);
        return label;
    }


    public @Nullable Label getNamedLabel(String labelName) {
        return this.namedLabels.get(labelName);
    }

    public @NotNull Label addNamedLabel(String labelName) {
        Label label = new Label(labelName);
        this.namedLabels.put(labelName, label);
        return label;
    }

    public @Nullable Label get(Inst target) {
        return this.labelsMap.get(target);
    }


}
