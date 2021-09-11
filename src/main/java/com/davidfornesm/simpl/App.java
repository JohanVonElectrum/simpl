package com.davidfornesm.simpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    static boolean hadError = true;

    public static void main( String[] args ) throws IOException
    {
        if (args.length > 1) {
            System.out.println("Usage: simpl [source]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
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
        for (Token token : tokens) {
            System.out.println(token);
        }
        Parser parser = new Parser(tokens);
        ProgramStatement program = parser.parse();

        System.out.println("program: " + program.toString());
        State finalState = program.eval(state);
        Integer result = finalState.lookup("result");
        System.out.println("result := " + result);
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
