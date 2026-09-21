package com.novapay.accounts.domain.exception;

import com.novapay.accounts.domain.model.vo.Money;

public class InsufficientBalanceException extends DomainException{

  public InsufficientBalanceException(Money balance, Money amount){
    super("Saldo insuficiente. Disponivel: %s, solicitado: %s".formatted(balance, amount));
  }
}
