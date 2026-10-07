package com.novapay.accounts.adapter.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.novapay.accounts.core.port.RegisterCustomerUseCase;
import com.novapay.accounts.core.port.AccountNumberGeneratorPort;
import com.novapay.accounts.core.port.AccountRepositoryPort;
import com.novapay.accounts.core.port.CustomerRepositoryPort;
import com.novapay.accounts.core.port.PasswordHasherPort;
import com.novapay.accounts.core.usecase.customer.RegisterCustomerService;

@Configuration
public class UseCaseConfig {

  @Bean
  public RegisterCustomerUseCase registerCustomerUseCase(
    CustomerRepositoryPort customerRepository,
    AccountRepositoryPort accountRepository,
    PasswordHasherPort passwordHasher,
    AccountNumberGeneratorPort accountNumberGenerator
  ){
    return new RegisterCustomerService(customerRepository, accountRepository, passwordHasher, accountNumberGenerator);
  }
}
