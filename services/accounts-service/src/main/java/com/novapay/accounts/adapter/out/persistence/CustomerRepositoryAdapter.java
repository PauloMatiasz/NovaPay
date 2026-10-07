package com.novapay.accounts.adapter.out.persistence;

import com.novapay.accounts.core.port.CustomerRepositoryPort;
import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.model.customer.Cpf;
import com.novapay.accounts.core.model.customer.Email;
import org.springframework.stereotype.Component;

@Component
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerJpaRepository jpaRepository;

    public CustomerRepositoryAdapter(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity saved = jpaRepository.save(CustomerPersistenceMapper.toEntity(customer));
        return CustomerPersistenceMapper.toDomain(saved);
    }

    @Override
    public boolean existsByCpf(Cpf cpf) {
        return jpaRepository.existsByCpf(cpf.value());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }
}
