package com.novapay.accounts.core.usecase.account;

import com.novapay.accounts.core.exception.AccountNotFoundException;
import com.novapay.accounts.core.model.account.Account;
import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.port.AccountRepositoryPort;
import com.novapay.accounts.core.port.CustomerRepositoryPort;
import com.novapay.accounts.core.port.GetMyAccountUseCase;
import com.novapay.accounts.core.port.MyAccountResult;

import java.util.UUID;

public class GetMyAccountService implements GetMyAccountUseCase {

    private final CustomerRepositoryPort customerRepository;
    private final AccountRepositoryPort accountRepository;

    public GetMyAccountService(CustomerRepositoryPort customerRepository,
                               AccountRepositoryPort accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public MyAccountResult getMyAccount(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(AccountNotFoundException::new);

        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(AccountNotFoundException::new);

        return new MyAccountResult(
                customer.fullName(),
                customer.cpf().masked(),
                customer.email().value(),
                account.number().value(),
                account.balance().value(),
                account.status().name());
    }
}