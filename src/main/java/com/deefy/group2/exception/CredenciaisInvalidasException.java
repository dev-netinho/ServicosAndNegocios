package com.deefy.group2.exception;

//Para erros de login (Segurança)
public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
