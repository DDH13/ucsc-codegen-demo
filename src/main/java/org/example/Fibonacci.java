package org.example;

import java.util.List;

public class Fibonacci {
    Stmt[] stmt = new Stmt[]{
            //  if (n == 0) {
            new IfStmt(
                    //list of condition blocks
                    List.of(new ConditionBlock(new IntEq(new Var("a"), new Int(0), "=="), new Stmt[]{new Return(new Int(0))}), new ConditionBlock(new IntEq(new Var("a"), new Int(1), "=="), new Stmt[]{new Return(new Int(1))})),
                    // } else {
                    new Stmt[]{
                            // return n * factorial(n - 1);
                            new Return(new IntOp(new FuncCall("fibonacci", new Expr[]{new IntOp(new Var("a"), new Int(1), "-")}), new FuncCall("fibonacci", new Expr[]{new IntOp(new Var("a"), new Int(2), "-")}), "+"))}
                    // }
            ),};
    Func func = new Func("fibonacci", new Var[]{new Var("a")}, stmt);
}
