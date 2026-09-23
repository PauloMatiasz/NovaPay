package com.novapay.accounts.domain.model;

import java.util.Objects;
import java.util.UUID;

import com.novapay.accounts.domain.exception.AccountNotActiveException;
import com.novapay.accounts.domain.exception.InsufficientBalanceException;
import com.novapay.accounts.domain.model.vo.AccountNumber;
import com.novapay.accounts.domain.model.vo.Money;

public class Account {

  private final UUID id;
  private final UUID customerId;
  private final AccountNumber number;
  private Money balance;
  private AccountStatus status;

  private Account(UUID id, UUID customerId, AccountNumber number, Money balance, AccountStatus status) {
    this.id = Objects.requireNonNull(id, "O id não pode ser nulo");
    this.customerId = Objects.requireNonNull(customerId, "O CustomerId não pode ser nulo");
    this.number = Objects.requireNonNull(number, "O número não pode ser nulo");
    this.balance = Objects.requireNonNull(balance, "O dinheiro não pode ser nulo");
    this.status = Objects.requireNonNull(status, "Status não pode ser nulo");
  }

  public static Account open(UUID customerId, AccountNumber number){
    return new Account(UUID.randomUUID(), customerId, number, Money.ZERO, AccountStatus.ACTIVE);
  }

  public static Account restore(UUID id, UUID customerId, AccountNumber number, Money balance, AccountStatus status){
    return new Account(id, customerId, number, balance, status);
  }

  public void credit(Money amount){

    if (amount.isZero()){
      throw new IllegalArgumentException("O valor não pode ser nulo");
    }
    if (!status.allowsCredit()){
      throw new AccountNotActiveException("Conta não esta ativada. Status Atual:" + status);
    }
    this.balance = this.balance.add(amount);
  }

  public void debit(Money amount){

    if (amount.isZero()){
      throw new IllegalArgumentException("O valor não pode ser nulo");
    }
    if (!status.allowsDebit()){
      throw new AccountNotActiveException("Conta não esta ativada. Status Atual:" + status);
    }
    if (balance.isLessThan(amount)){
      throw new InsufficientBalanceException(balance, amount);
    }
    this.balance = this.balance.subtract(amount);
  }

  public void block(){
     this.status = AccountStatus.BLOCKED;
  }

  public void unblock(){
    this.status = AccountStatus.ACTIVE;
  }

  public void close(){
    if (!balance.isZero()){
      throw new IllegalStateException("O saldo não é zero");
    }
    this.status = AccountStatus.CLOSED;
  }

  public boolean hasSufficientBalance(Money amount){
    return !balance.isLessThan(amount);
  }

  public UUID id(){
    return id;
  }

  public UUID customerId(){
    return customerId;
  }

  public AccountNumber number(){
    return number;
  }

  public Money balance(){
    return balance;
  }

  public AccountStatus status(){
    return status;
  }

  @Override
  public boolean equals(Object other){
    if (this == other){
      return true;
    }
    if (!(other instanceof Account account)){
      return false;
    }
    return id.equals(account.id);
  }

  @Override
  public int hashCode(){
    return id.hashCode();
  }

  @Override
  public String toString() {
      return "Account[number=%s, status=%s]".formatted(number, status);
  }
}

