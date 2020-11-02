package com.ciberman.fastacompiler.ir;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class IRProgram {

    private final List<Inst> instList = new ArrayList<>();
    private final List<BranchInst> branchesList = new ArrayList<>();

    private final Set<IntConst> intConsts = new HashSet<>();
    private final Set<LongConst> longConsts = new HashSet<>();
    private final Set<StrConst> strConsts = new HashSet<>();

    private Scope currentScope = null;

    private final Map<String, Symbol> symbolMap = new HashMap<>();

    /**
     * Push an instruction into the instructions list
     * @param inst The instruction to push
     * @param <T> The type of the instruction
     * @return The same provided instruction, for easy chaining.
     */
    private <T extends Inst> T pushInstr(T inst) {
        this.instList.add(inst);
        return inst;
    }

    public void enterScope(String name) {
        this.currentScope = new Scope(name, this.currentScope);
    }

    public void exitScope() {
        this.currentScope = this.currentScope.getParent();
    }

    /**
     * Adds a symbol to the symbol table.
     * @param name The symbol name
     * @param type The symbol type
     */
    public void declareSymbol(String name, ValueType type) {
        String scopedName = this.currentScope.getName(name);
        this.symbolMap.put(scopedName, new Symbol(scopedName, type));
    }

    /**
     * Registers the given constant in the constants table
     * @param intConst The constant
     */
    public IntConst addConst(int intConst) {
        IntConst c = new IntConst(intConst);
        this.intConsts.add(c);
        return c;
    }

    /**
     * Registers the given constant in the constants table
     * @param longConst The constant
     */
    public LongConst addConst(long longConst) {
        LongConst c = new LongConst(longConst);
        this.longConsts.add(c);
        return c;
    }

    /**
     * Registers the given constant in the constants table
     * @param strConst The constant
     */
    public StrConst addConst(String strConst) {
        StrConst c = new StrConst(strConst);
        this.strConsts.add(c);
        return c;
    }

    /**
     * Adds a new "add" instruction to the program
     * @param op1 The first operand
     * @param op2 The second operand
     * @return The newly created instruction
     */
    public AddInst createAddInst(Value op1, Value op2) {
        return this.pushInstr(new AddInst(op1, op2));
    }

    /**
     * Adds a new "sub" instruction to the program
     * @param op1 The first operand
     * @param op2 The second operand
     * @return The newly created instruction
     */
    public SubInst createSubInst(Value op1, Value op2) {
        return this.pushInstr(new SubInst(op1, op2));
    }

    /**
     * Adds a new "mul" instruction to the program
     * @param op1 The first operand
     * @param op2 The second operand
     * @return The newly created instruction
     */
    public MulInst createMulInst(Value op1, Value op2) {
        return this.pushInstr(new MulInst(op1, op2));
    }

    /**
     * Adds a new "div" instruction to the program
     * @param op1 The first operand
     * @param op2 The second operand
     * @return The newly created instruction
     */
    public DivInst createDivInst(Value op1, Value op2) {
        return this.pushInstr(new DivInst(op1, op2));
    }

    /**
     * Adds a new "neg" instruction to the program
     * @param op The operand
     * @return The newly created instruction
     */
    public NegInst createNegInst(Value op) {
        return this.pushInstr(new NegInst(op));
    }

    /**
     * Adds a new "itol" instruction to the program
     * @param op The operand
     * @return The newly created instruction
     */
    public ItolInst createItolInst(Value op) {
        return this.pushInstr(new ItolInst(op));
    }

    /**
     * Adds a new "print" instruction to the program
     * @param op The operand
     * @return The newly created instruction
     */
    public PrintInst createPrintInst(StrConst op) {
        return this.pushInstr(new PrintInst(op));
    }


    /**
     * Creates a bool expression
     * @param lhs Left hand side of the expression
     * @param operator The relational operator
     * @param rhs Right hand side of the expression
     * @return The bool expression
     */
    public BranchCondition createBranchCondition(Value lhs, BranchCondition.RelOperator operator, Value rhs) {
        return new BranchCondition(lhs, operator, rhs);
    }

    public BranchInst createBranchInst(BranchCondition branchCondition) {
        BranchInst branchInst = new BranchInst(branchCondition, null);
        this.branchesList.add(branchInst);
        return this.pushInstr(branchInst);
    }

    public BranchInst createBranchInst() {
        BranchInst branchInst = new BranchInst(null);
        this.branchesList.add(branchInst);
        return this.pushInstr(branchInst);
    }

    public AssignInst createAssignInst(Symbol symbol, Value value) {
        return this.pushInstr(new AssignInst(symbol, value));
    }

    public NoOpInst createNoOpInst() {
        return this.pushInstr(new NoOpInst());
    }
    /**
     * @return The last instruction added to the program
     */
    public Inst getLastInst() {
        return this.instList.get(this.instList.size() - 1);
    }

    /**
     * Finds a symbol by name
     * @param name The name of the symbol
     * @return The symbol, or null if not found
     */
    public @Nullable Symbol findSymbolByName(String name) {
        Symbol symbol = null;
        Scope scope = this.currentScope;

        while ((scope != null) && (symbol = this.symbolMap.get(scope.getName(name))) == null) {
            scope = scope.getParent();
        }

        return symbol;
    }

    public int getInstIndex(Inst inst) {
        return this.instList.indexOf(inst);
    }

    public Iterable<IntConst> intConsts() {
        return this.intConsts;
    }

    public Iterable<LongConst> longConsts() {
        return this.longConsts;
    }

    public Iterable<StrConst> strConsts() {
        return this.strConsts;
    }

    public Iterable<Symbol> symbols() {
        return this.symbolMap.values();
    }

    public Iterable<Inst> instructions() {
        return instList;
    }

    public Iterable<BranchInst> branches() {
        return branchesList;
    }

}
