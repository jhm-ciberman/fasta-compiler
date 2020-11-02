package com.ciberman.fastacompiler.asm;

import java.util.LinkedList;
import java.util.List;

public class MasmOutput {

    List<String> dataSegment = new LinkedList<>();
    List<String> codeSegment = new LinkedList<>();

    protected void push(String str) {
        System.out.println(str);
    }

    public void generate() {
        this.push(".386");
        this.push(".model flat, stdcall");
        this.push("option casemap :none");
        this.push("include \\masm32\\include\\msvcrt.inc");
        this.push("includelib \\masm32\\lib\\msvcrt.lib");
        this.push(".data");
        for (String str : this.dataSegment) {
            this.push(str);
        }
        this.push(".code");
        this.push("start:");
        for (String str : this.codeSegment) {
            this.push(str);
        }
        this.push("ret");
        this.push("end start");
    }

    public void addDataDeclaration(String s) {
        this.dataSegment.add(s);
    }

    public void addCode(String opName, Location loc1, Location loc2) {
        this.codeSegment.add(opName + " " + loc1.getName() + "," + loc2.getName());
    }

    public void addCode(String opName, Location loc) {
        this.codeSegment.add(opName + " " + loc.getName());
    }

    public void addPrint(Location loc) {
        this.codeSegment.add("invoke crt_printf,addr " + loc.getName());
    }

    public void addLabel(String name) {
        this.codeSegment.add(name + ":");
    }

    public void addJump(String jmp, String s) {
        this.codeSegment.add(jmp + " " + s);
    }
}
