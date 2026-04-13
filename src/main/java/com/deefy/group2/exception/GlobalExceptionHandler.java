package com.deefy.group2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // // Trata tentativas de cadastro com e-mails já existentes na base
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleEmailConflict(EmailJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "400",
                "error", "Erro de Cadastro",
                "message", ex.getMessage()
        ));
    }

    // Trata falhas de autenticação (e-mail ou senha incorretos)
    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, String>> handleAuthError(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "status", "401",
                "error", "Acesso Negado",
                "message", ex.getMessage()
        ));
    }

    // Trata erros de validação das anotações @Valid nos Controllers
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Pegamos a primeira mensagem de erro que as anotações geraram
        String mensagemErro = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "400",
                "error", "Dados Inválidos",
                "message", mensagemErro // Aqui aparecerá "Formato de e-mail inválido", etc.
        ));
    }

    // Filtro de segurança para qualquer outro erro inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralError(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Erro Interno",
                "message", "Ocorreu um problema no servidor. Contate o administrador."
        ));
    }
}