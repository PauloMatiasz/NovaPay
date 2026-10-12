package com.novapay.accounts.adapter.in.controller;

import com.novapay.accounts.adapter.in.api.ErrorResponse;
import com.novapay.accounts.core.exception.CustomerAlreadyExistsException;
import com.novapay.accounts.core.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExists(CustomerAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        // se aparecer riscado no IntelliJ, troque por UNPROCESSABLE_CONTENT
        return build(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        FieldError erro = ex.getBindingResult().getFieldError();
        assert erro != null;
        return build(HttpStatus.BAD_REQUEST, erro.getField() + ": " + erro.getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "JSON inválido");
    }

    @ExceptionHandler(Exception.class) // rede de segurança: qualquer erro não previsto
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Erro inesperado", ex); // stack trace vai pro log, não pro cliente
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno. Tente novamente.");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), mensagem));
    }
}