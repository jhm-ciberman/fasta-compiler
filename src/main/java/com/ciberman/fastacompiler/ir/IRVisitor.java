package com.ciberman.fastacompiler.ir;

public interface IRVisitor {
    void addInst(AddInst instr);
    void subInst(SubInst instr);
    void mulInst(MulInst instr);
    void divInst(DivInst instr);
    void negInst(NegInst instr);
    void itolInst(ItolInst instr);
    void printInst(PrintInst instr);
    void branchInst(BranchInst instr);
    void noopInst(NoOpInst instr);
    void assignInst(AssignInst instr);

}
