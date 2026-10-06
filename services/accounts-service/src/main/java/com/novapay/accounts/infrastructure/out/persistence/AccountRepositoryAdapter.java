package com.novapay.accounts.infrastructure.out.persistence;

import com.novapay.accounts.application.port.out.AccountRepositoryPort;
import com.novapay.accounts.domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountJpaRepository jpaRepository;

    public AccountRepositoryAdapter(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = AccountPersistenceMapper.toEntity(account);
        AccountJpaEntity saved = jpaRepository.save(entity);
        return AccountPersistenceMapper.toDomain(saved);
    }
}
