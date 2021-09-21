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
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        ProgramStatement program = parser.parse();
        State state = new State();
        State root = program.eval(state);
        assertEquals(Integer.valueOf(29), root.lookup("result"));
    }
}