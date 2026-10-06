package com.novapay.accounts.infrastructure.out.persistence;

import org.springframework.stereotype.Component;

import com.novapay.accounts.application.port.out.AccountNumberGeneratorPort;
import com.novapay.accounts.domain.model.vo.AccountNumber;

@Component
public class AccountNumberGeneratorAdapter implements AccountNumberGeneratorPort{

  private static final String DEFAULT_BRANCH = "0001";

  private final AccountJpaRepository jpaRepository;

  public AccountNumberGeneratorAdapter(AccountJpaRepository jpaRepository){
    this.jpaRepository = jpaRepository;
  }

  @Override
  public AccountNumber next(){
    Long sequence = jpaRepository.nextAccountNumber();
    String number = "%08d".formatted(sequence);
    return AccountNumber.of(DEFAULT_BRANCH, number);
  }
}
