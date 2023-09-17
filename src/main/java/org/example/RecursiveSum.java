package org.example;

import java.util.List;

public class RecursiveSum {
    Stmt[] stmt = new Stmt[]{
            //  if (n == 0) {
            new IfStmt(
                    //list of condition blocks
                    List.of(new ConditionBlock(new IntEq(new Var("a"), new Int(0), "=="), new Stmt[]{new Return(new Int(0))})),
                    // } else {
                    new Stmt[]{
                            // return n + sum(n - 1);
                            new Return(new IntOp(new Var("a"), new FuncCall("recursivesum", new Expr[]{new IntOp(new Var("a"), new Int(1), "-")}), "+"))}
                    // }
            ),};
    Func func = new Func("recursivesum", new Var[]{new Var("a")}, stmt);
}
