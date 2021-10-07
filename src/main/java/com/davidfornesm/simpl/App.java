package com.davidfornesm.simpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Main App Class
 * Here is the entry point.
 * This class cannot be instantiated and contains only static
 * methods to drive the most abstract layers and application startup.
 */
public abstract class App
{
    static boolean hadError = true;
    static boolean debug = false;
    static boolean compile = false;

    public static void main( String[] args ) throws IOException
    {
        //TODO: argument parsing with structure schemas (maybe some regex)
        if (args.length > 2) {
            System.err.println("Usage: \n\t- simpl\n\t- simpl debug\n\t- simpl [file]\n\t- simpl [file] compile\n\t- simpl [file] debug");
            System.exit(64);
        } else if (args.length > 0 && !args[0].equals("debug")) {
            debug = args.length == 2 && args[1].equals("debug");
            compile = args.length == 2 && args[1].equals("compile");
            runFile(args[0]);
        } else {
            debug = args.length == 1;
            runPrompt();
        }
    }

    private static void runPrompt() throws IOException {
        State state = new State();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Run 'quit()' to exit CLI.");

        while (true) {
            System.out.print(">> ");
            String line = scanner.nextLine();
            if (line.equals("quit()")) break;
            try {
                run(line, state);
            } catch (RuntimeException e) {
                System.err.println(e);
                System.err.println("Error: " + e.getMessage());
            }
        }
    }


    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()), null);
        if (hadError)
            System.exit(65);
    }

    private static void run(String source, State state) {
        if (state == null) state = new State();
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
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
            compileVars.append("int ").append(k).append(";\n");
        }
        System.out.println("#include <stdio.h>\n" +
                "int main() {\n" + compileVars.toString() + compileSource + " " +
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
