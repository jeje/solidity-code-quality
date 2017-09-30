package com.talanlabs.solidity.model;

import java.util.Objects;

public class ValidationError {
    private final String code;
    private final ValidationErrorCriticity criticity;
    private final String message;
    private final Position start;
    private final Position stop;

    public ValidationError(String code, ValidationErrorCriticity criticity, String message, Position start, Position stop) {
        this.code = code;
        this.criticity = criticity;
        this.message = message;
        this.start = start;
        this.stop = stop;
    }

    public String getCode() {
        return code;
    }

    public ValidationErrorCriticity getCriticity() {
        return criticity;
    }

    public String getMessage() {
        return message;
    }

    public Position getStart() {
        return start;
    }

    public Position getStop() {
        return stop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return Objects.equals(code, that.code) &&
                criticity == that.criticity &&
                Objects.equals(message, that.message) &&
                Objects.equals(start, that.start) &&
                Objects.equals(stop, that.stop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, criticity, message, start, stop);
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "code='" + code + '\'' +
                ", criticity=" + criticity +
                ", message='" + message + '\'' +
                ", start=" + start +
                ", stop=" + stop +
                '}';
    }
}
