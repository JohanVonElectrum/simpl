package com.davidfornesm.simpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Hello world!
 *
 */
public class App 
{
    static boolean hadError = true;
    static boolean debug = false;
    static boolean compile = false;

    public static void main( String[] args ) throws IOException
    {
        if (args.length > 2) {
            System.out.println("Usage: simpl debug || [source] compile || [source] (debug)");
            System.exit(64);
        } else if ((args.length == 1 && !Objects.equals(args[0], "debug")) || args.length == 2) {
            if (args.length == 2 && Objects.equals(args[1], "debug")) debug = true;
            if (args.length == 2 && Objects.equals(args[1], "compile")) compile = true;
            runFile(args[0]);
        } else {
            if (args.length == 1 && args[0].equals("debug")) debug = true;
            runPrompt();
        }
    }

    private static void runPrompt() throws IOException {
        State state = new State();
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print(">> ");
            String line = reader.readLine();
            if (line == null) break;
            try {
                run(line, state);
            } catch (RuntimeException e) {
                System.out.println(e);
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()), null);
        if (hadError) System.exit(65);
    }

    private static void run(String source, State state) {
        if (state == null) state = new State();
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        ProgramStatement program = parser.parse();
        State finalState = program.eval(state);
        Integer result = finalState.lookup("result");

        if (debug) emitDebug(tokens, program);
        if (!compile) System.out.println("result := " + result);
        if (compile) compileSourceCode(program);
    }

    private static void emitDebug(List<Token> tokens, ProgramStatement program) {
        for (Token token : tokens) {
            System.out.println(token);
        }
        System.out.println("program: " + program.toString());
    }

    private static void compileSourceCode(ProgramStatement program) {
        State compileState = new State();
        String compileSource = program.compile(compileState);
        StringBuilder compileVars = new StringBuilder();
        for (String k :
                compileState.keys()) {
            compileVars.append("int ").append(k).append(";");
        }
        System.out.println("#include <stdio.h>\n" +
                "int main() {" + compileVars.toString() + compileSource + " " +
                "printf(\"result := %d\\n\", result);" +
                "}");
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
