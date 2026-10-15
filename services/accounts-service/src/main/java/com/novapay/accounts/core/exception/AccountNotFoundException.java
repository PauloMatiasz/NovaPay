package com.novapay.accounts.core.exception;

public class AccountNotFoundException extends DomainException {
    public AccountNotFoundException() {
        super("Conta não encontrada");
    }
}
