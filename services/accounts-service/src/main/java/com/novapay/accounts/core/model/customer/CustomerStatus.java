package com.novapay.accounts.core.model.customer;


public enum CustomerStatus{

  ACTIVE,
  INACTIVE;

  public boolean isActive(){
    return this == ACTIVE;
  }

  public boolean isInactive(){
    return this == INACTIVE;
  }
}
