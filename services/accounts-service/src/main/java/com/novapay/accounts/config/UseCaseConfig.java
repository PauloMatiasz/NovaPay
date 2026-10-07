package com.novapay.accounts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.novapay.accounts.application.port.in.RegisterCustomerUseCase;
import com.novapay.accounts.application.port.out.AccountNumberGeneratorPort;
import com.novapay.accounts.application.port.out.AccountRepositoryPort;
import com.novapay.accounts.application.port.out.CustomerRepositoryPort;
import com.novapay.accounts.application.port.out.PasswordHasherPort;
import com.novapay.accounts.application.usecase.RegisterCustomerService;

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
