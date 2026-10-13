package com.novapay.accounts.core.usecase.auth;

import com.novapay.accounts.core.exception.InvalidCredentialsException;
import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.model.customer.Email;
import com.novapay.accounts.core.port.*;

public class LoginService implements LoginUseCase {

    private final CustomerRepositoryPort customerRepository;
    private final PasswordHasherPort passwordHasher;
    private final TokenGeneratorPort tokenGenerator;

    public LoginService(CustomerRepositoryPort customerRepository,
                        PasswordHasherPort passwordHasher,
                        TokenGeneratorPort tokenGenerator) {
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public LoginResult login(LoginCommand command) {
        Customer customer = customerRepository.findByEmail(Email.of(command.email()))
                .orElseThrow(() -> new InvalidCredentialsException("Credencias invalidas"));

        if (!passwordHasher.matches(command.password(), customer.passwordHash())) {
            throw new InvalidCredentialsException("Senha invalida");
        }

        return new LoginResult(tokenGenerator.generate(customer));
    }
}