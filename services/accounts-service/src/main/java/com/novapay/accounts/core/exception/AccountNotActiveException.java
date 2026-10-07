package com.novapay.accounts.core.exception;

public class AccountNotActiveException extends DomainException {

  public AccountNotActiveException(String status){
    super("Conta não está ativa. Status atual: " + status);
  }
}
