package com.novapay.accounts.core.port;

import com.novapay.accounts.core.model.account.Account;

public interface AccountRepositoryPort {

  Account save(Account account);
}
