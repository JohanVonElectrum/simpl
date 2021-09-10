package com.davidfornesm.simpl;

abstract class ProgramStatement {

    public abstract State eval(State s); // <i,s> |v| s'

    static class EmptyStmt extends ProgramStatement {

        public State eval(State s) {
            System.out.println("SKIP");
            return s;
        }
    }


    static class AssignmentStmt extends ProgramStatement {

        private final String x; // a variable is a numeric index
        private final ArithmeticExp a;

        public AssignmentStmt(String name, ArithmeticExp e) {
            x = name;
            a = e;
        }

        public State eval(State s) {
            s.setNewBinding(x, a.eval(s));
            return s;
        }
    }

    static class SequenceStmt extends ProgramStatement {

        private final ProgramStatement c1;
        private final ProgramStatement c2;

        public SequenceStmt(ProgramStatement s1, ProgramStatement s2) {
            c1 = s1;
            c2 = s2;
        }

        public State eval(State s) {
            return c2.eval(c1.eval(s));
        }
    }

    static class IfThenElseStmt extends ProgramStatement {

        private final BooleanExp b;
        private final ProgramStatement c1;
        private final ProgramStatement c2;

        public IfThenElseStmt(BooleanExp e, ProgramStatement s1, ProgramStatement s2) {
            b = e;
            c1 = s1;
            c2 = s2;
        }

        public State eval(State s) {
            if (b.eval(s)) return c1.eval(s);
            else return c2.eval(s);
        }
    }

    static class WhileStmt extends ProgramStatement {

        private final BooleanExp b;
        private final ProgramStatement c;

        public WhileStmt(BooleanExp e, ProgramStatement s) {
            b = e;
            c = s;
        }

        public State eval(State s) {
            if (b.eval(s)) return this.eval(c.eval(s));
            else return s;
        }
    }
}
