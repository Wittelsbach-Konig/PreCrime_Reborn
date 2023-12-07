package ru.itmo.backend.exceptions;

public class InvalidRoleException extends RuntimeException{
    private static final long serialVerisionUID = 3;

    public InvalidRoleException(String message) { super(message); }
}
