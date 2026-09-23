package com.novapay.accounts.application.port.in;

public record RegisterCustomerCommand(String fullName, String cpf, String email, String password) {
  
}
