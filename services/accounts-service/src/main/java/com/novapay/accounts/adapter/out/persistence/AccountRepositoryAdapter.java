package com.novapay.accounts.adapter.out.persistence;

import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.port.AccountRepositoryPort;
import com.novapay.accounts.core.model.account.Account;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<Account> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId)
                .map(AccountPersistenceMapper::toDomain);
    }
}
