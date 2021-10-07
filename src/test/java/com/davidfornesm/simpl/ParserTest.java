package com.davidfornesm.simpl;

import junit.framework.TestCase;

import java.util.List;

public class ParserTest extends TestCase {

    public void testParse() {
        String source = "result := 1; " +
                "if result <= 0 || 2 * 75 <= 1 + 500 + 0 " +
                "then result := 27 " +
                "else (); " +
                "i := 0; " +
                "while i + 1 <= 5" +
                "do (result := result + 2 * 0; i := i + 1); " +
                "if false || !!!false " +
                "then while result <= 28" +
                "do (result := result + 1); " +
                "else (); ";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        ProgramStatement program = parser.parse();
        State state = new State();
        State root = program.eval(state);
        assertEquals(Integer.valueOf(29), root.lookup("result"));
    }

    public void testParse2() {
        String source = "" +
                "result := 0; \n" +
                "while result <= 6 do ( \n" +
                "    result := result + 2; \n" +
                "    a := result \n" +
                "); \n" +
                "if a > 10 \n" +
                "then result := a + a \n" +
                "else ( \n" +
                "a := 200 + a; \n" +
                "result := a; \n" +
                "); \n" +
                "result := 1000 + result; \n" +
                "skip";
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        ProgramStatement program = parser.parse();
        State state = new State();
        State root = program.eval(state);
        assertEquals(Integer.valueOf(1208), root.lookup("result"));
    }
}