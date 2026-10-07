package com.novapay.accounts.application.usecase;

import com.novapay.accounts.application.port.in.RegisterCustomerCommand;
import com.novapay.accounts.application.port.in.RegisterCustomerResult;
import com.novapay.accounts.application.port.in.RegisterCustomerUseCase;
import com.novapay.accounts.application.port.out.AccountNumberGeneratorPort;
import com.novapay.accounts.application.port.out.AccountRepositoryPort;
import com.novapay.accounts.application.port.out.CustomerRepositoryPort;
import com.novapay.accounts.application.port.out.PasswordHasherPort;
import com.novapay.accounts.domain.exception.CustomerAlreadyExistsException;
import com.novapay.accounts.domain.model.Account;
import com.novapay.accounts.domain.model.Customer;
import com.novapay.accounts.domain.model.vo.AccountNumber;
import com.novapay.accounts.domain.model.vo.Cpf;
import com.novapay.accounts.domain.model.vo.Email;

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
