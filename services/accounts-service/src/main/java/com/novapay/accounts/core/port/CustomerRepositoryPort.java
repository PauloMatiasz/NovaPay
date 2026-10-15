package com.novapay.accounts.core.port;

import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.model.customer.Cpf;
import com.novapay.accounts.core.model.customer.Email;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {

  Customer save(Customer customer);

  boolean existsByCpf(Cpf cpf);
  Optional<Customer> findByEmail(Email email);
  boolean existsByEmail(Email email);

  Optional<Customer> findById(UUID customerId);
}
