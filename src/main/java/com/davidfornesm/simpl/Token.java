package com.davidfornesm.simpl;

import java.util.Objects;

public class Token {
    public enum TokenType {
        NUMBER, IDENTIFIER, PLUS, MINUS, STAR,
        TRUE, FALSE, EQUAL, LESS_EQUAL, NOT, OR,
        SKIP, WALRUS, SEMICOLON, IF, THEN, ELSE, WHILE, DO,
        END,
        EOF
    }

    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) return false;
        Token other = (Token) obj;
        return Objects.equals(type, other.type) &&
                Objects.equals(lexeme, other.lexeme) &&
                Objects.equals(literal, other.literal) &&
                Objects.equals(line, other.line);
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                ", literal=" + literal +
                ", line=" + line +
                '}';
    }
}
