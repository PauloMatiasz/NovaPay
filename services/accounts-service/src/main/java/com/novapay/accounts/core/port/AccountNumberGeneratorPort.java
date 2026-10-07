package com.novapay.accounts.core.port;

import com.novapay.accounts.core.model.account.AccountNumber;

public interface AccountNumberGeneratorPort {

  AccountNumber next();
}
