package com.noriservices.norisales.shared.exception;

public class UnauthorizedUserException extends RuntimeException{
    public UnauthorizedUserException(String message){
        super(message);
    }
}
