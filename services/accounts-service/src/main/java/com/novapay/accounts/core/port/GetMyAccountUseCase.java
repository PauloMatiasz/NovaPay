package com.novapay.accounts.core.port;

import java.util.UUID;

public interface GetMyAccountUseCase {

    MyAccountResult getMyAccount(UUID customerId);
}
