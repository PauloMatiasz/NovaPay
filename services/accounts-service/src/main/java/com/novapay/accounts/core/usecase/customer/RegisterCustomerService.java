package com.novapay.accounts.core.usecase.customer;

import com.novapay.accounts.core.port.RegisterCustomerCommand;
import com.novapay.accounts.core.port.RegisterCustomerResult;
import com.novapay.accounts.core.port.RegisterCustomerUseCase;
import com.novapay.accounts.core.port.AccountNumberGeneratorPort;
import com.novapay.accounts.core.port.AccountRepositoryPort;
import com.novapay.accounts.core.port.CustomerRepositoryPort;
import com.novapay.accounts.core.port.PasswordHasherPort;
import com.novapay.accounts.core.exception.CustomerAlreadyExistsException;
import com.novapay.accounts.core.model.account.Account;
import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.model.account.AccountNumber;
import com.novapay.accounts.core.model.customer.Cpf;
import com.novapay.accounts.core.model.customer.Email;

import jakarta.transaction.Transactional;

public class RegisterCustomerService implements RegisterCustomerUseCase {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private final CustomerRepositoryPort customerRepository;
    private final AccountRepositoryPort accountRepository;
    private final PasswordHasherPort passwordHasher;
    private final AccountNumberGeneratorPort accountNumberGenerator;

    public RegisterCustomerService(CustomerRepositoryPort customerRepository,
                                   AccountRepositoryPort accountRepository,
                                   PasswordHasherPort passwordHasher,
                                   AccountNumberGeneratorPort accountNumberGenerator) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.passwordHasher = passwordHasher;
        this.accountNumberGenerator = accountNumberGenerator;
    }


    @Override
    @Transactional
    public RegisterCustomerResult execute(RegisterCustomerCommand command) {
        validatePassword(command.password());

        Cpf cpf = Cpf.of(command.cpf());
        Email email = Email.of(command.email());

        if (customerRepository.existsByCpf(cpf)) {
            throw new CustomerAlreadyExistsException("CPF");
        }
        if (customerRepository.existsByEmail(email)) {
            throw new CustomerAlreadyExistsException("email");
        }

        String passwordHash = passwordHasher.hash(command.password());
        Customer customer = Customer.register(command.fullName(), cpf, email, passwordHash);
        Customer savedCustomer = customerRepository.save(customer);

        AccountNumber number = accountNumberGenerator.next();
        Account account = Account.open(savedCustomer.id(), number);
        Account savedAccount = accountRepository.save(account);

        return new RegisterCustomerResult(
                savedCustomer.id(),
                savedAccount.id(),
                savedAccount.number().formatted());
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Senha deve ter ao menos 8 caracteres");
        }
    }
}
