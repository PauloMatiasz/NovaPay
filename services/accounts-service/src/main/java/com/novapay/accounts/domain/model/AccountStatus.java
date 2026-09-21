package com.novapay.accounts.domain.model;

public enum AccountStatus {

  ACTIVE,
  BLOCKED,
  CLOSED;

  public boolean isActive(){
    return this == ACTIVE;
  }

  public boolean allowsDebit(){
    return isActive();
  }

  public boolean allowsCredit(){
    return this != CLOSED;
  }
}
