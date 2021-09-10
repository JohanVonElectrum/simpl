package com.davidfornesm.simpl;

abstract class ArithmeticExp {

    public abstract int eval(State s); // <a,s> => n

    static class VariableExp extends ArithmeticExp {
        private String name; // a variable is a numeric index

        public VariableExp(String index)
        { name = index; }

        public int eval(State s)
        {  return s.lookup(name);
        }
    }

    static class NumericExp extends ArithmeticExp {
        private int n;

        public NumericExp(int m)
        { n = m; }

        public int eval(State s)
        {  return n; }
    }

    static class AdditionExp extends ArithmeticExp {
        private ArithmeticExp a1,a2;

        public AdditionExp(ArithmeticExp e1, ArithmeticExp e2)
        { a1 = e1; a2 = e2; }

        public int eval(State s)
        {  return a1.eval(s) + a2.eval(s); }
    }

    static class SubtractionExp extends ArithmeticExp {
        private ArithmeticExp a1,a2;

        public SubtractionExp(ArithmeticExp e1, ArithmeticExp e2)
        { a1 = e1; a2 = e2; }

        public int eval(State s)
        {  return a1.eval(s) - a2.eval(s); }
    }

    static class ProductExp extends ArithmeticExp {
        private ArithmeticExp a1,a2;

        public ProductExp(ArithmeticExp e1, ArithmeticExp e2)
        { a1 = e1; a2 = e2; }

        public int eval(State s)
        {  return a1.eval(s) * a2.eval(s); }
    }
}

//
// Boolean Expressions
//

abstract class BooleanExp {

    public abstract boolean eval(State s); // <b,s> => v

    static class TrueExp extends BooleanExp {

        public boolean eval(State s)
        {  return true; }
    }

    static class FalseExp extends BooleanExp {

        public boolean eval(State s)
        {  return false; }
    }

    static class EqualExp extends BooleanExp {
        private ArithmeticExp a1,a2;

        public EqualExp(ArithmeticExp e1, ArithmeticExp e2)
        { a1 = e1; a2 = e2; }

        public boolean eval(State s)
        {  return a1.eval(s) == a2.eval(s); }
    }

    static class LeqExp extends BooleanExp {
        private ArithmeticExp a1,a2;

        public LeqExp(ArithmeticExp e1, ArithmeticExp e2)
        { a1 = e1; a2 = e2; }

        public boolean eval(State s)
        {  return a1.eval(s) <= a2.eval(s); }
    }

    class BiggerThanExp extends BooleanExp {
        private ArithmeticExp a1,a2;

        public BiggerThanExp(ArithmeticExp e1, ArithmeticExp e2)
        { a1 = e1; a2 = e2; }

        public boolean eval(State s)
        {  return a1.eval(s) > a2.eval(s); }
    }

    static class NotExp extends BooleanExp {

        private BooleanExp b;

        public NotExp(BooleanExp e)
        { b = e; }

        public boolean eval(State s)
        {  return ! (b.eval(s)); }
    }

    static class OrExp extends BooleanExp {

        private BooleanExp b1,b2;

        public OrExp(BooleanExp e1, BooleanExp e2)
        { b1 = e1; b2 = e2; }

        public boolean eval(State s)
        {  boolean b;

            b = b1.eval(s);
            if (b) return b;
            else return b2.eval(s);
        }
    }
}

