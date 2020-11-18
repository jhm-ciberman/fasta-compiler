package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.mem.MemDeclaration;
import com.ciberman.fastacompiler.asm.program.*;
import com.ciberman.fastacompiler.asm.program.AsmVisitor;

import java.io.FileWriter;
import java.io.IOException;

public class MasmOutput implements AsmVisitor {

    private FileWriter writer;

    private final String indent = "    ";

    public void generate(AsmProgram asm, String filename) throws IOException {
        this.writer = new FileWriter(filename);
        writer.write(".386\n");
        writer.write(".model flat, stdcall\n");
        writer.write("option casemap :none\n");
        writer.write("include \\masm32\\include\\msvcrt.inc\n");
        writer.write("includelib \\masm32\\lib\\msvcrt.lib\n");
        writer.write(".data\n");
        for (MemDeclaration memDeclaration : asm.getDataSegment()) {
            writer.write(this.indent +  memDeclaration.toString() + '\n');
        }
        writer.write(".code\n");
        writer.write("start:\n");
        for (AsmCode codeSegment : asm.getCodeSegment()) {
            codeSegment.accept(this);
        }
        writer.write("end start\n");
        writer.close();
    }

    @Override
    public void op(AsmOp asmOp) throws IOException {
        String str = this.indent +  asmOp.getType();
        Location loc1 = asmOp.getLoc1();
        Location loc2 = asmOp.getLoc2();
        if (loc1 != null) str += " " + loc1.getName();
        if (loc2 != null) str += ", " + loc2.getName();
        this.writer.write(str + '\n');
    }

    @Override
    public void comment(AsmComment comment) throws IOException {
        this.writer.write(this.indent +  comment.getText() + '\n');
    }

    @Override
    public void label(AsmLabel label) throws IOException {
        this.writer.write(this.indent +  label.getLabel() + ":\n");
    }

    @Override
    public void jump(AsmJump asmJump) throws IOException {
        this.writer.write(this.indent +  asmJump.getJumpType() + " " + asmJump.getLabel() + '\n');
    }

    @Override
    public void print(AsmPrint asmPrint) throws IOException {
        String str = this.indent +  "invoke crt_printf, addr " + asmPrint.getFormat();
        if (asmPrint.getArg() != null) {
            str += ", " + asmPrint.getArg();
        }
        this.writer.write(str + "\n");
    }
}
