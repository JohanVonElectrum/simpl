package com.davidfornesm.simpl;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import static com.davidfornesm.simpl.Token.TokenType.*;

public class ScannerTest extends TestCase {

    public void testScanTokens() {
        String source = "123 abc + - * true false " +
                "= <= > ! || skip := ; if then else " +
                "while do ( )";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        List<Token> expected_tokens = new ArrayList<>();
        expected_tokens.add(new Token(NUMBER, "123", 123, 1));
        expected_tokens.add(new Token(IDENTIFIER, "abc", null, 1));
        expected_tokens.add(new Token(PLUS, "+", null, 1));
        expected_tokens.add(new Token(MINUS, "-", null, 1));
        expected_tokens.add(new Token(STAR, "*", null, 1));
        expected_tokens.add(new Token(TRUE, "true", null, 1));
        expected_tokens.add(new Token(FALSE, "false", null, 1));
        expected_tokens.add(new Token(EQUAL, "=", null, 1));
        expected_tokens.add(new Token(LESS_EQUAL, "<=", null, 1));
        expected_tokens.add(new Token(MORE, ">", null, 1));
        expected_tokens.add(new Token(NOT, "!", null, 1));
        expected_tokens.add(new Token(OR, "||", null, 1));
        expected_tokens.add(new Token(SKIP, "skip", null, 1));
        expected_tokens.add(new Token(WALRUS, ":=", null, 1));
        expected_tokens.add(new Token(SEMICOLON, ";", null, 1));
        expected_tokens.add(new Token(IF, "if", null, 1));
        expected_tokens.add(new Token(THEN, "then", null, 1));
        expected_tokens.add(new Token(ELSE, "else", null, 1));
        expected_tokens.add(new Token(WHILE, "while", null, 1));
        expected_tokens.add(new Token(DO, "do", null, 1));
        expected_tokens.add(new Token(LEFT_PAREN, "(", null, 1));
        expected_tokens.add(new Token(RIGHT_PAREN, ")", null, 1));
        expected_tokens.add(new Token(EOF, "", null, 1));

        assertEquals(expected_tokens, tokens);
    }
}