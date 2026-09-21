package com.novapay.accounts.domain.exception;

import com.novapay.accounts.domain.model.vo.Money;

public class DailyLimitExceededException extends DomainException{

  public DailyLimitExceededException(Money limit, Money amount){
    super("Limite diario excedido. Limite: %s, solicitado: %s".formatted(limit, amount));
  }
}
