package com.novapay.accounts.core.exception;

import com.novapay.accounts.core.model.account.Money;

public class InsufficientBalanceException extends DomainException {

  public InsufficientBalanceException(Money balance, Money amount){
    super("Saldo insuficiente. Disponivel: %s, solicitado: %s".formatted(balance, amount));
  }
}
