package com.novapay.accounts.core.port;

import com.novapay.accounts.core.model.account.Account;
import com.novapay.accounts.core.model.customer.Customer;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {

  Account save(Account account);

    Optional<Account> findByCustomerId(UUID customerId);
}
