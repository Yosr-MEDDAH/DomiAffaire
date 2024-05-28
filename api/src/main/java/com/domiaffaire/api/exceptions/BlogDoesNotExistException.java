package com.domiaffaire.api.exceptions;

public class BlogDoesNotExistException extends Exception {
    public BlogDoesNotExistException(String message){
        super(message);
    }
}
