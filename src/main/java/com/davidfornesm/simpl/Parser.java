package com.davidfornesm.simpl;

import java.util.List;

import static com.davidfornesm.simpl.Token.TokenType.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    ProgramStatement parse() {
        return program();
    }

    private ProgramStatement program() {
        return programStatement();
    }

    private ProgramStatement programStatement() {
        ProgramStatement programStatement = skipStatement();
        if (match(SKIP)) programStatement = skipStatement();
        if (match(IDENTIFIER)) programStatement = assignmentStatement();
        if (match(IF)) programStatement =  ifStatement();
        if (match(WHILE)) programStatement = whileStatement();
        if (match(SEMICOLON)) programStatement = new ProgramStatement.SequenceStmt(programStatement, programStatement());
        return programStatement;
    }

    private ProgramStatement skipStatement() {
        return new ProgramStatement.EmptyStmt();
    }

    private ProgramStatement assignmentStatement() {
        Token ident = previous();
        String name = ident.lexeme;
        consume(WALRUS, "expected walrus operator.");
        ArithmeticExp expr = arithmeticExp();
        return new ProgramStatement.AssignmentStmt(name, expr);
    }

    private ProgramStatement ifStatement() {
        BooleanExp booleanExp = booleanExp();
        consume(THEN, "expected then after if.");
        ProgramStatement ifTrue = programStatement();
        consume(ELSE, "expected else after then.");
        ProgramStatement ifFalse = programStatement();
        ProgramStatement.IfThenElseStmt ifThenElseStmt = new ProgramStatement.IfThenElseStmt(booleanExp, ifTrue, ifFalse);
        consume(END, "expected end after else.");
        return ifThenElseStmt;

    }

    private ProgramStatement whileStatement() {
        BooleanExp booleanExp = booleanExp();
        consume(DO, "expected do after while.");
        ProgramStatement programStatement = programStatement();
        ProgramStatement.WhileStmt whileStmt = new ProgramStatement.WhileStmt(booleanExp, programStatement);
        consume(END, "expected end after while");
        return whileStmt;
    }

    private BooleanExp booleanExp() {
        if (match(TRUE)) {
            if (match(OR)) {
                BooleanExp booleanExp = booleanExp();
                return new BooleanExp.OrExp(new BooleanExp.TrueExp(), booleanExp);
            } else {
                return new BooleanExp.TrueExp();
            }
        }
        if (match(FALSE)) {
            if (match(OR)) {
                BooleanExp booleanExp = booleanExp();
                return new BooleanExp.OrExp(new BooleanExp.FalseExp(), booleanExp);
            } else {
                return new BooleanExp.FalseExp();
            }
        }
        if (match(NOT)) {
            BooleanExp booleanExp = booleanExp();
            if (match(OR)) {
                return new BooleanExp.OrExp(booleanExp, booleanExp());
            }
            return booleanExp;
        }
        ArithmeticExp arithmeticExp = arithmeticExp();
        if (match(EQUAL)) {
            ArithmeticExp arithmeticExp2 = arithmeticExp();
            BooleanExp.EqualExp equalExp = new BooleanExp.EqualExp(arithmeticExp, arithmeticExp2);
            if (match(OR)) {
                return new BooleanExp.OrExp(equalExp, booleanExp());
            }
            return equalExp;

        }
        if (match(LESS_EQUAL)) {
            BooleanExp.LeqExp leqExp = new BooleanExp.LeqExp(arithmeticExp, arithmeticExp());
            if (match(OR)) {
                return new BooleanExp.OrExp(leqExp, booleanExp());
            }
            return leqExp;

        }

        throw new RuntimeException();
    }

    private ArithmeticExp arithmeticExp() {
        if (match(NUMBER)) {
            ArithmeticExp arithmeticExp = new ArithmeticExp.NumericExp((Integer) previous().literal);
            if (match(PLUS)) {
                return new ArithmeticExp.AdditionExp(arithmeticExp,
                        arithmeticExp());
            } else if (match(MINUS)) {
                return new ArithmeticExp.SubtractionExp(arithmeticExp, arithmeticExp());
            } else if (match(STAR)) {
                return new ArithmeticExp.ProductExp(arithmeticExp, arithmeticExp());
            }
            return arithmeticExp;
        }
        if (match(IDENTIFIER)) {
            ArithmeticExp arithmeticExp = new ArithmeticExp.VariableExp(previous().lexeme);
            if (match(PLUS)) {
                return new ArithmeticExp.AdditionExp(arithmeticExp, arithmeticExp());
            }  else if (match(MINUS)) {
                return new ArithmeticExp.SubtractionExp(arithmeticExp, arithmeticExp());
            } else if (match(STAR)) {
                return new ArithmeticExp.ProductExp(arithmeticExp, arithmeticExp());
            }
            else {
                return arithmeticExp;
            }
        }
        throw new RuntimeException();
    }

    private ArithmeticExp numericExp() {
        return new ArithmeticExp.NumericExp((Integer) previous().literal);
    }

    private boolean match(Token.TokenType... types) {
        for (Token.TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(Token.TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message);
    }

    private boolean check(Token.TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if(!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

}
