package com.ciberman.fastacompiler.asm.program;

import com.ciberman.fastacompiler.asm.Location;
import com.ciberman.fastacompiler.asm.labels.Label;
import com.ciberman.fastacompiler.asm.mem.MemDeclaration;
import com.ciberman.fastacompiler.asm.mem.MemLocation;

import java.util.LinkedList;
import java.util.List;

public class AsmProgram {

    private final List<MemDeclaration> dataSegment = new LinkedList<>();
    private final List<AsmCode> codeSegment = new LinkedList<>();

    public void addCode(String type, Location loc1, Location loc2) {
        this.codeSegment.add(new AsmOp(type, loc1, loc2));
    }

    public void addCode(String type, Location loc) {
        this.codeSegment.add(new AsmOp(type, loc));
    }

    public void addCode(String type) {
        this.codeSegment.add(new AsmOp(type));
    }

    public void addData(MemDeclaration data) {
        this.dataSegment.add(data);
    }

    public void addLabel(Label label) {
        this.codeSegment.add(new AsmLabel(label));
    }

    public void addPrint(MemLocation loc) {
        this.codeSegment.add(new AsmPrint(loc));
    }

    public void addPrint(MemLocation formatLoc, Location valueLoc) {
        this.codeSegment.add(new AsmPrint(formatLoc, valueLoc));
    }

    public void addJump(String jumpType, Label label) {
        this.codeSegment.add(new AsmJump(jumpType, label));
    }

    public void addComment(String text) {
        this.codeSegment.add(new AsmComment(text));
    }

    public Iterable<MemDeclaration> getDataSegment() {
        return dataSegment;
    }

    public Iterable<AsmCode> getCodeSegment() {
        return codeSegment;
    }

    public void printDebugString() {
        System.out.println("Data segment: ");
        for (MemDeclaration data : this.dataSegment) {
            System.out.println(data);
        }
        System.out.println("Code segment: ");
        for (AsmCode code : this.codeSegment) {
            System.out.println(code);
        }
    }
}
