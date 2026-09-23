package com.novapay.accounts.application.port.out;

import com.novapay.accounts.domain.model.Account;

public interface AccountRepositoryPort {

  Account save(Account account);
}
