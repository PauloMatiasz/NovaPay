package com.novapay.accounts.application.port.out;

import com.novapay.accounts.domain.model.vo.AccountNumber;

public interface AccountNumberGeneratorPort {

  AccountNumber next();
}
