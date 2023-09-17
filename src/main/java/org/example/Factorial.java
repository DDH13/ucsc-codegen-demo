package org.example;

import java.util.List;

public class Factorial {
    Stmt[] stmt = new Stmt[]{
            //  if (n == 0) {
            new IfStmt(
                    //list of condition blocks
                    List.of(new ConditionBlock(new IntEq(new Var("a"), new Int(0), "=="), new Stmt[]{new Return(new Int(1))})),
                    // } else {
                    new Stmt[]{
                            // return n * factorial(n - 1);
                            new Return(new IntOp(new Var("a"), new FuncCall("factorial", new Expr[]{new IntOp(new Var("a"), new Int(1), "-")}), "*"))}
                    // }
            ),};
    Func func = new Func("factorial", new Var[]{new Var("a")}, stmt);
}
