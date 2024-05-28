package com.domiaffaire.api.exceptions;

public class BlogNotFoundException extends Exception {
    public BlogNotFoundException(String message){
        super(message);
    }
}
