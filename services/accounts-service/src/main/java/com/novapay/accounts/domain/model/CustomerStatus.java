package com.novapay.accounts.domain.model;


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
