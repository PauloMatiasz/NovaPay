package com.novapay.accounts.domain.exception;

public class AccountNotActiveException extends DomainException{

  public AccountNotActiveException(String status){
    super("Conta não está ativa. Status atual: " + status);
  }
}
