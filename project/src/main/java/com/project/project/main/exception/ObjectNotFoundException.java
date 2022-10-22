package com.project.project.main.exception;

public class ObjectNotFoundException extends RuntimeException{
    private String message;

    public ObjectNotFoundException(String msg){
        super(msg);
        this.message = msg;
    }
}
