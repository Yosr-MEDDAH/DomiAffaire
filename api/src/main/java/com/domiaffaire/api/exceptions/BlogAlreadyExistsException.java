package com.domiaffaire.api.exceptions;

public class BlogAlreadyExistsException extends Exception {
    public BlogAlreadyExistsException(String message){
        super(message);
    }
}
