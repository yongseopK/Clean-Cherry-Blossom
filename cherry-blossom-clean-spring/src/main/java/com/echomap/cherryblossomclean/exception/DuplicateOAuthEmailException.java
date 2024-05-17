package com.echomap.cherryblossomclean.exception;

public class DuplicateOAuthEmailException extends RuntimeException {
    public DuplicateOAuthEmailException(String message) {
        super(message);
    }
}
