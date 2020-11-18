package com.ciberman.fastacompiler.asm;

import com.ciberman.fastacompiler.asm.program.AsmProgram;
import com.ciberman.fastacompiler.ir.BranchInst;
import com.ciberman.fastacompiler.ir.IRProgram;
import com.ciberman.fastacompiler.ir.Inst;

public class AsmGenerator {

    private final LocationResolver resolver;
    private final AsmBuilder builder;

    public AsmGenerator() {
        this.resolver = new LocationResolver();
        this.builder = new AsmBuilder(this.resolver);
    }

    public LocationResolver getResolver() {
        return resolver;
    }

    public AsmBuilder getBuilder() {
        return builder;
    }

    public AsmProgram generate(IRProgram program) {
        AsmVisitor visitor = new AsmVisitor(this.builder, this.resolver);

        for (BranchInst branch : program.branches()) {
            Inst target = branch.getTarget();
            if (target != null) {
                this.resolver.addLabel(target);
            }
        }

        for (Inst instr : program.instructions()) {
            visitor.processInstr(instr);
        }

        return builder.build();
    }
}
