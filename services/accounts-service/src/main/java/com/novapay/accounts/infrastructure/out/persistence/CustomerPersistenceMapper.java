package com.novapay.accounts.infrastructure.out.persistence;

import com.novapay.accounts.domain.model.Customer;
import com.novapay.accounts.domain.model.vo.Cpf;
import com.novapay.accounts.domain.model.vo.Email;

final class CustomerPersistenceMapper {

    private CustomerPersistenceMapper() {
    }

    static CustomerJpaEntity toEntity(Customer customer) {
        CustomerJpaEntity entity = new CustomerJpaEntity();
        entity.setId(customer.id());
        entity.setFullName(customer.fullName());
        entity.setCpf(customer.cpf().value());
        entity.setEmail(customer.email().value());
        entity.setPasswordHash(customer.passwordHash());
        entity.setStatus(customer.status());
        entity.setCreatedAt(customer.createdAt());
        return entity;
    }

    static Customer toDomain(CustomerJpaEntity entity) {
        return Customer.restore(
                entity.getId(),
                entity.getFullName(),
                Cpf.of(entity.getCpf()),
                Email.of(entity.getEmail()),
                entity.getPasswordHash(),
                entity.getStatus(),
                entity.getCreatedAt());
    }
}
