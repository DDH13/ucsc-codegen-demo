package org.example;

public class Add {
    Stmt[] stmt = new Stmt[]{
            new Return(new IntOp(new IntOp(new Var("a"), new Var("b"), "+"), new Var("c"), "+"))
    };
    Func func = new Func("add", new Var[]{new Var("a"), new Var("b"), new Var("c")}, stmt);
}
