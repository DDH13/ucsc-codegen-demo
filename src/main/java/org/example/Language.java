package org.example;


import java.util.List;

class Expr {
}

class Stmt {
}

class Var extends Expr {
    String name;

    public Var(String name) {
        this.name = name;
    }
}

class Func {
    public Func(String name, Var[] args, Stmt[] stmts) {
        this.name = name;
        this.args = args;
        this.stmts = stmts;
    }

    public String name;
    Var[] args;
    Stmt[] stmts;

}

class FuncCall extends Expr {
    String name;
    Expr[] args;

    public FuncCall(String name, Expr[] args) {
        this.name = name;
        this.args = args;
    }
}

class IfStmt extends Stmt {
    List<ConditionBlock> conditionBlocks;
    Stmt[] elseStmts; // Default else statements

    public IfStmt(List<ConditionBlock> conditionBlocks, Stmt[] elseStmts) {
        this.conditionBlocks = conditionBlocks;
        this.elseStmts = elseStmts;
    }
}

class ConditionBlock {
    Expr condition;
    Stmt[] stmts;

    public ConditionBlock(Expr condition, Stmt[] stmts) {
        this.condition = condition;
        this.stmts = stmts;
    }
}

class Return extends Stmt {
    Expr expr;

    public Return(Expr expr) {
        this.expr = expr;
    }
}

class Int extends Expr {
    int value;

    public Int(int value) {
        this.value = value;
    }
}

class IntEq extends Expr {
    Expr left;
    Expr right;
    String op;

    public IntEq(Expr left, Expr right, String op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }
}

class IntOp extends Expr {
    Expr left;
    Expr right;
    String op;

    public IntOp(Expr left, Expr right, String op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }
}
