package com.novapay.accounts.core.port;

import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.model.customer.Cpf;
import com.novapay.accounts.core.model.customer.Email;

public interface CustomerRepositoryPort {

  Customer save(Customer customer);

  boolean existsByCpf(Cpf cpf);
  
  boolean existsByEmail(Email email);
}
