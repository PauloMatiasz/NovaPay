package com.novapay.accounts.application.port.out;

import com.novapay.accounts.domain.model.Customer;
import com.novapay.accounts.domain.model.vo.Cpf;
import com.novapay.accounts.domain.model.vo.Email;

public interface CustomerRepositoryPort {

  Customer save(Customer customer);

  boolean existsByCpf(Cpf cpf);
  
  boolean existsByEmail(Email email);
}
