package com.talanlabs.solidity.model;

import org.antlr.v4.runtime.Token;

import java.util.Objects;

public class Position {
    private final int line;
    private final int column;

    public Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public static Position start(Token token) {
        return new Position(token.getLine(), token.getCharPositionInLine() + 1);
    }

    public static Position stop(Token token) {
        int column = token.getCharPositionInLine() + 1 + token.getStopIndex() - token.getStartIndex();
        return new Position(token.getLine(), column);
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return line == position.line &&
                column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }

    @Override
    public String toString() {
        return "Position{" +
                "line=" + line +
                ", column=" + column +
                '}';
    }
}
