package com.novapay.accounts.application.port.in;

import java.util.UUID;

public record RegisterCustomerResult(UUID customerId, UUID accountId, String accountNumber) {

}
