package com.davidfornesm.simpl;

abstract class ArithmeticExp {

    public abstract int eval(State s);

    static class VariableExp extends ArithmeticExp {
        private final String name;

        public VariableExp(String index) {
            name = index;
        }

        @Override
        public String toString() {
            return "(var: " + name + ")";
        }

        public int eval(State s) {
            return s.lookup(name);
        }
    }

    static class NumericExp extends ArithmeticExp {
        private final int n;

        public NumericExp(int m) {
            n = m;
        }

        @Override
        public String toString() {
            return "(num: " + n + ")";
        }

        public int eval(State s) {
            return n;
        }
    }

    static class AdditionExp extends ArithmeticExp {
        private final ArithmeticExp a1;
        private final ArithmeticExp a2;

        public AdditionExp(ArithmeticExp e1, ArithmeticExp e2) {
            a1 = e1;
            a2 = e2;
        }

        @Override
        public String toString() {
            return "(add: "  + a1.toString() + ", " + a2.toString() + ")";
        }

        public int eval(State s) {
            return a1.eval(s) + a2.eval(s);
        }
    }

    static class SubtractionExp extends ArithmeticExp {
        private final ArithmeticExp a1;
        private final ArithmeticExp a2;

        public SubtractionExp(ArithmeticExp e1, ArithmeticExp e2) {
            a1 = e1;
            a2 = e2;
        }

        @Override
        public String toString() {
            return "(sub: " + a1.toString() + ", " + a2.toString() + ")";
        }

        public int eval(State s) {
            return a1.eval(s) - a2.eval(s);
        }
    }

    static class ProductExp extends ArithmeticExp {
        private final ArithmeticExp a1;
        private final ArithmeticExp a2;

        public ProductExp(ArithmeticExp e1, ArithmeticExp e2) {
            a1 = e1;
            a2 = e2;
        }

        @Override
        public String toString() {
            return "(prod: " + a1.toString() + ", " + a2.toString() + ")";
        }

        public int eval(State s) {
            return a1.eval(s) * a2.eval(s);
        }
    }
}

//
// Boolean Expressions
//

abstract class BooleanExp {

    public abstract boolean eval(State s); // <b,s> => v

    static class TrueExp extends BooleanExp {

        @Override
        public String toString() {
            return "(true)";
        }

        public boolean eval(State s) {
            return true;
        }
    }

    static class FalseExp extends BooleanExp {

        @Override
        public String toString() {
            return "(false)";
        }

        public boolean eval(State s) {
            return false;
        }
    }

    static class EqualExp extends BooleanExp {
        private final ArithmeticExp a1;
        private final ArithmeticExp a2;

        public EqualExp(ArithmeticExp e1, ArithmeticExp e2) {
            a1 = e1;
            a2 = e2;
        }

        @Override
        public String toString() {
            return "(equal: " + a1.toString() +  ", " + a2.toString() + ")";
        }

        public boolean eval(State s) {
            return a1.eval(s) == a2.eval(s);
        }
    }

    static class LeqExp extends BooleanExp {
        private final ArithmeticExp a1;
        private final ArithmeticExp a2;

        public LeqExp(ArithmeticExp e1, ArithmeticExp e2) {
            a1 = e1;
            a2 = e2;
        }

        @Override
        public String toString() {
            return "(leq: " + a1.toString() + ", " + a2.toString() + ")";
        }

        public boolean eval(State s) {
            return a1.eval(s) <= a2.eval(s);
        }
    }

    static class BiggerThanExp extends BooleanExp {
        private final ArithmeticExp a1;
        private final ArithmeticExp a2;

        public BiggerThanExp(ArithmeticExp e1, ArithmeticExp e2) {
            a1 = e1;
            a2 = e2;
        }

        @Override
        public String toString() {
            return "(bigger: " + a1.toString() + ", " + a2.toString() + ")";
        }

        public boolean eval(State s) {
            return a1.eval(s) > a2.eval(s);
        }
    }

    static class NotExp extends BooleanExp {

        private final BooleanExp b;

        public NotExp(BooleanExp e) {
            b = e;
        }

        @Override
        public String toString() {
            return "(not: " + b.toString() + ";";
        }

        public boolean eval(State s) {
            return !(b.eval(s));
        }
    }

    static class OrExp extends BooleanExp {

        private final BooleanExp b1;
        private final BooleanExp b2;

        public OrExp(BooleanExp e1, BooleanExp e2) {
            b1 = e1;
            b2 = e2;
        }

        @Override
        public String toString() {
            return "(or: " + b1.toString() + ", " + b2.toString() + ")";
        }

        public boolean eval(State s) {
            boolean b;

            b = b1.eval(s);
            if (b) return b;
            else return b2.eval(s);
        }
    }
}

