package com.novapay.accounts.adapter.in.api;

import java.util.UUID;

public record RegisterCustomerResponse(
        UUID customerId,
        UUID accountId,
        String accountNumber
) {
}