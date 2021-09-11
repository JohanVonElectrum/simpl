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
        else if (match(IDENTIFIER)) programStatement = assignmentStatement();
        else if (match(IF)) programStatement =  ifStatement();
        else if (match(WHILE)) programStatement = whileStatement();
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
        BooleanExp booleanExp = null;
        if (match(TRUE)) booleanExp = new BooleanExp.TrueExp();
        else if (match(FALSE)) booleanExp = new BooleanExp.FalseExp();
        else if (match(NOT)) booleanExp = new BooleanExp.NotExp(booleanExp());
        else {
            ArithmeticExp arithmeticExp = arithmeticExp();
            if (match(EQUAL)) {
                booleanExp = new BooleanExp.EqualExp(arithmeticExp, arithmeticExp());
            } else if (match(LESS_EQUAL)) {
                booleanExp = new BooleanExp.LeqExp(arithmeticExp, arithmeticExp());
            } else if (match(MORE)) {
                booleanExp = new BooleanExp.BiggerThanExp(arithmeticExp, arithmeticExp());
            }
        }
        if (booleanExp == null) throw new RuntimeException("expected boolean expression.");
        if (match(OR)) booleanExp = new BooleanExp.OrExp(booleanExp, booleanExp());
        return booleanExp;
    }

    private ArithmeticExp arithmeticExp() {
        ArithmeticExp arithmeticExp = null;
        if (match(NUMBER)) arithmeticExp = new ArithmeticExp.NumericExp((Integer) previous().literal);
        else if (match(IDENTIFIER)) arithmeticExp = new ArithmeticExp.VariableExp(previous().lexeme);
        if (arithmeticExp == null) throw new RuntimeException("expected arithmetic expression.");
        if (match(PLUS, MINUS, STAR)) {
            switch (previous().type) {
                case PLUS: arithmeticExp = new ArithmeticExp.AdditionExp(arithmeticExp, arithmeticExp()); break;
                case MINUS: arithmeticExp =  new ArithmeticExp.SubtractionExp(arithmeticExp, arithmeticExp()); break;
                case STAR: arithmeticExp = new ArithmeticExp.ProductExp(arithmeticExp, arithmeticExp()); break;
            }
        }
        return arithmeticExp;
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

    private void consume(Token.TokenType type, String message) {
        if (check(type)) {
            advance();
            return;
        }
        throw new RuntimeException(message);
    }

    private boolean check(Token.TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private void advance() {
        if(!isAtEnd()) current++;
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
