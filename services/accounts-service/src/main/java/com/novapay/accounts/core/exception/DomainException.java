package com.novapay.accounts.core.exception;

public abstract class DomainException extends RuntimeException {

  protected DomainException(String message){
    super(message);
  }
}
