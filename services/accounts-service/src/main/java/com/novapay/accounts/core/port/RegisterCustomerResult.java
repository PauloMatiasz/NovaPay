package com.novapay.accounts.core.port;

import java.util.UUID;

public record RegisterCustomerResult(UUID customerId, UUID accountId, String accountNumber) {

}
