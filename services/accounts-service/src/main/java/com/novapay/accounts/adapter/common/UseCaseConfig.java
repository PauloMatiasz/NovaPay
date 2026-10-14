package com.novapay.accounts.adapter.common;

import com.novapay.accounts.core.port.*;
import com.novapay.accounts.core.usecase.auth.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

  @Bean
  public LoginUseCase loginUseCase(CustomerRepositoryPort customerRepository,
                                   PasswordHasherPort passwordHasher,
                                   TokenGeneratorPort tokenGenerator) {
    return new LoginService(customerRepository, passwordHasher, tokenGenerator);
  }
}
