package org.example;

import java.util.List;

public class Gcd {

Stmt[] stmt = new Stmt[]{
            //  if (b == 0) {
            new IfStmt(
                    //list of condition blocks
                    List.of(new ConditionBlock(new IntEq(new Var("b"), new Int(0), "=="), new Stmt[]{new Return(new Var("a"))})),
                    // } else {
                    new Stmt[]{
                            // return gcd(b, a % b);
                            new Return(new FuncCall("gcd", new Expr[]{new Var("b"), new IntOp(new Var("a"), new Var("b"), "%")}))}
                    // }
            ),};

    Func func = new Func("gcd", new Var[]{new Var("a"), new Var("b")}, stmt);
}
