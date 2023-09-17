package org.example;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.util.List;

import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;


public class Main {
    public static void main(String[] args) {

        //list of functions
        Func[] funcs = new Func[]{new Fibonacci().func, new Factorial().func, new Gcd().func};

        for (Func func : funcs) {
            byte[] bytes = codeGen(func);

            try {
                FileOutputStream fos = new FileOutputStream(func.name + ".class");
                fos.write(bytes);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] codeGen(Func func) {
        ClassWriter classWriter = new ClassWriter(COMPUTE_MAXS);
        classWriter.visit(V17, ACC_PUBLIC | ACC_SUPER, func.name, null, "java/lang/Object", null);
        genInit(classWriter);
        genFunc(func, classWriter);
        genFixedMain(func, classWriter);
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    private static void genInit(ClassWriter classWriter) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
    }

    private static void genFixedMain(Func func, ClassWriter classWriter) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        methodVisitor.visitCode();
        for (int i = 0; i < func.args.length; i++) {
            parseArg(methodVisitor, i, i + 1);
        }
        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        for (int i = 0; i < func.args.length; i++) {
            methodVisitor.visitVarInsn(ILOAD, i + 1);
        }
        methodVisitor.visitMethodInsn(INVOKESTATIC, func.name, func.name, genSig(func.args.length), false);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    private static void parseArg(MethodVisitor methodVisitor, int argIndex, int varIndex) {
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitIntInsn(BIPUSH, argIndex);
        methodVisitor.visitInsn(AALOAD);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
        methodVisitor.visitVarInsn(ISTORE, varIndex);
    }

    private static void genFunc(Func func, ClassWriter classWriter) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, func.name, genSig(func.args.length), null, null);
        methodVisitor.visitCode();
        for (Stmt s : func.stmts) {
            if (s instanceof Return) {
                genReturn((Return) s, methodVisitor, func.args);
            } else if (s instanceof IfStmt) {
                genIf((IfStmt) s, methodVisitor, func.args);
            }
        }

        methodVisitor.visitMaxs(0, 0); // You might need to calculate max stack and locals
        methodVisitor.visitEnd();
    }


    private static void genReturn(Return s, MethodVisitor methodVisitor, Var[] args) {
        genExpr(s.expr, methodVisitor, args);
        methodVisitor.visitInsn(IRETURN);
    }

    private static void genExpr(Expr expr, MethodVisitor methodVisitor, Var[] args) {
        if (expr instanceof IntOp) {
            genExpr(((IntOp) expr).left, methodVisitor, args);
            genExpr(((IntOp) expr).right, methodVisitor, args);
            switch (((IntOp) expr).op) {
                case "+" -> methodVisitor.visitInsn(IADD);
                case "-" -> methodVisitor.visitInsn(ISUB);
                case "*" -> methodVisitor.visitInsn(IMUL);
                case "%" -> methodVisitor.visitInsn(IREM);
            }
        } else if (expr instanceof Int) {
            methodVisitor.visitIntInsn(BIPUSH, ((Int) expr).value);
        } else if (expr instanceof Var) {
            methodVisitor.visitVarInsn(ILOAD, getIndex((Var) expr, args));
        } else if (expr instanceof IntEq) {
            genExpr(((IntEq) expr).left, methodVisitor, args);
            genExpr(((IntEq) expr).right, methodVisitor, args);
            Label label1 = new Label();
            Label label2 = new Label();
            if (((IntEq) expr).op.equals("==")) {
                methodVisitor.visitJumpInsn(IF_ICMPEQ, label1);
            } else if (((IntEq) expr).op.equals("!=")) {
                methodVisitor.visitJumpInsn(IF_ICMPNE, label1);
            }
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitJumpInsn(GOTO, label2);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
        } else if (expr instanceof FuncCall) {
            genFnCall((FuncCall) expr, methodVisitor, args);
        }
    }

    private static void genIf(IfStmt s, MethodVisitor methodVisitor, Var[] args) {
        for (ConditionBlock conditionBlock : s.conditionBlocks) {
            genExpr(conditionBlock.condition, methodVisitor, args);
            Label label1 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, label1);  // Change this line
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            for (Stmt stmt : conditionBlock.stmts) {
                if (stmt instanceof Return) {
                    genReturn((Return) stmt, methodVisitor, args);
                }
            }
            methodVisitor.visitLabel(label1);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        }
        methodVisitor.visitLabel(new Label());
        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        for (Stmt stmt : s.elseStmts) {
            if (stmt instanceof Return) {
                genReturn((Return) stmt, methodVisitor, args);
            }
        }
    }


    private static void genFnCall(FuncCall s, MethodVisitor methodVisitor, Var[] args) {
        for (Expr expr : s.args) {
            genExpr(expr, methodVisitor, args);
        }
        methodVisitor.visitMethodInsn(INVOKESTATIC, s.name, s.name, genSig(s.args.length), false);
    }

    private static int getIndex(Var v, Var[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].name.equals((v).name)) {
                return i;
            }
        }
        return -1;
    }

    private static String genSig(int length) {
        return "(" + "I".repeat(Math.max(0, length)) + ")I";
    }


}