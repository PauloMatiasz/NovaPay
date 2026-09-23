package com.novapay.accounts.domain.exception;

public class CustomerAlreadyExistsException extends DomainException{

  public CustomerAlreadyExistsException(String field){
    super("Ja existe cliente cadastrado com este " + field);
  }
}
