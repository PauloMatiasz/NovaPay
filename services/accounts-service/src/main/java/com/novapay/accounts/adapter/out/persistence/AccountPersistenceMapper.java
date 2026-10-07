package com.novapay.accounts.adapter.out.persistence;

import com.novapay.accounts.core.model.account.Account;
import com.novapay.accounts.core.model.account.AccountNumber;
import com.novapay.accounts.core.model.account.Money;

final class AccountPersistenceMapper {

  private AccountPersistenceMapper(){

  }

  static AccountJpaEntity toEntity(Account account){
    AccountJpaEntity entity = new AccountJpaEntity();
    entity.setId(account.id());
    entity.setCustomerId(account.customerId());
    entity.setNumber(account.number().value());
    entity.setBalance(account.balance().value());
    entity.setStatus(account.status());
    return entity;
  }

  static Account toDomain(AccountJpaEntity entity){
    return Account.restore(entity.getId(),
    entity.getCustomerId(),
    AccountNumber.of(entity.getNumber()),
    Money.of(entity.getBalance()),
    entity.getStatus());
  }
}
