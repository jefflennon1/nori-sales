package com.noriservices.norisales.shared.exception;

public class ProductQuantityInvalidException extends RuntimeException{
    public ProductQuantityInvalidException(String message){
        super(message);
    }
}
