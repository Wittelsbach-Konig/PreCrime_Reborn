package ru.itmo.backend.exceptions;

public class NotValidArgumentException extends RuntimeException{
    private static final long serialVerisionUID = 1;
    public NotValidArgumentException(String message) {
        super(message);
    }
}
