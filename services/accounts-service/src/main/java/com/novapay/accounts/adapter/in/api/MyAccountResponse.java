package com.novapay.accounts.adapter.in.api;

import java.math.BigDecimal;

public record MyAccountResponse(String fullName, String cpf, String email, String accountNumber, BigDecimal balance, String status) {
}
