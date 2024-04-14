    package com.domiaffaire.api.exceptions;

    public class WrongPasswordException extends Exception {
        public WrongPasswordException(String message){
            super(message);
        }
    }
