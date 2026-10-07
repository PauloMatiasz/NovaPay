package com.novapay.accounts.core.port;

public record RegisterCustomerCommand(String fullName, String cpf, String email, String password) {
  
}
