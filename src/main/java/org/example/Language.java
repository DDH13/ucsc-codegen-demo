package org.example;

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

class Expr {
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

class Stmt {
}

class Return extends Stmt {
    Expr expr;

    public Return(Expr expr) {
        this.expr = expr;
    }
}