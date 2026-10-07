package com.novapay.accounts.core.exception;

import com.novapay.accounts.core.model.account.Money;

public class DailyLimitExceededException extends DomainException {

  public DailyLimitExceededException(Money limit, Money amount){
    super("Limite diario excedido. Limite: %s, solicitado: %s".formatted(limit, amount));
  }
}
