package ru.cherniak.menuvotingsystem.util.exception;

public class OutsideTimeException extends RuntimeException {

    public OutsideTimeException(String message) {
        super(message);
    }
}