package com.ciberman.fastacompiler.asm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MasmOutput {

    List<String> dataSegment = new LinkedList<>();
    List<String> codeSegment = new LinkedList<>();
    private boolean useDivisionByZeroErrorLabel = false;
    private boolean useOverflowErrorLabel = false;
    private final String indent = "    ";

    public void generate(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(".386\n");
        writer.write(".model flat, stdcall\n");
        writer.write("option casemap :none\n");
        writer.write("include \\masm32\\include\\msvcrt.inc\n");
        writer.write("includelib \\masm32\\lib\\msvcrt.lib\n");
        writer.write(".data\n");
        for (String str : this.dataSegment) {
            writer.write(this.indent +  str + '\n');
        }
        if (this.useDivisionByZeroErrorLabel) {
            writer.write(this.indent + "@div_by_zero_error db 'ERROR: Division by zero.', 10, 0\n");
        }
        if (this.useOverflowErrorLabel) {
            writer.write(this.indent + "@overflow_error db 'ERROR: Overflow in addition.', 10, 0\n");
        }
        writer.write(".code\n");
        writer.write("start:\n");
        for (String str : this.codeSegment) {
            writer.write(str + '\n');
        }
        writer.write(this.indent + "ret\n");
        if (this.useDivisionByZeroErrorLabel) {
            writer.write("label_error_division_by_zero:\n");
            writer.write(this.indent + "invoke crt_printf,addr @div_by_zero_error\n");
            writer.write(this.indent + "ret\n");
        }
        if (this.useOverflowErrorLabel) {
            writer.write("label_error_overflow:\n");
            writer.write(this.indent + "invoke crt_printf,addr @overflow_error\n");
            writer.write(this.indent + "ret\n");
        }
        writer.write("end start\n");
        writer.close();
    }

    private void pushCode(String string) {
        this.codeSegment.add(string);
    }

    public void addDataDeclaration(MemDeclaration declaration) {
        this.dataSegment.add(declaration.toString());
    }

    public void addCode(String opName, Location loc1, Location loc2) {
        this.pushCode(this.indent + opName + " " + loc1.getName() + "," + loc2.getName());
    }

    public void addCode(String opName, Location loc) {
        this.pushCode(this.indent + opName + " " + loc.getName());
    }

    public void addCode(String opName) {
        this.pushCode(this.indent + opName);
    }

    public void addComment(String comment) {
        this.pushCode(this.indent + "; " + comment);
    }


    public void addPrint(Location loc) {
        this.pushCode(this.indent + "invoke crt_printf,addr " + loc.getName());
    }

    public void addPrint(Location formatLoc, Location valueLoc) {
        this.pushCode(this.indent + "invoke crt_printf,addr " + formatLoc.getName() + ", " + valueLoc.getName());
    }

    public void addLabel(String name) {
        this.pushCode(name + ":");
    }

    public void addJump(String jmp, String s) {
        this.pushCode(this.indent + jmp + " " + s);
    }

    public String getDivisionByZeroErrorLabel() {
        this.useDivisionByZeroErrorLabel = true;
        return "label_error_division_by_zero";
    }

    public String getOverflowErrorLabel() {
        this.useOverflowErrorLabel = true;
        return "label_error_overflow";
    }
}
