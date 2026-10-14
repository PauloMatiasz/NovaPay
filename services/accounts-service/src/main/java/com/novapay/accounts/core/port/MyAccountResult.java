package com.novapay.accounts.core.port;

import java.math.BigDecimal;

public record MyAccountResult(String fullName, String maskedCpf, String email, String accountNumber, BigDecimal balance, String status) {
}
