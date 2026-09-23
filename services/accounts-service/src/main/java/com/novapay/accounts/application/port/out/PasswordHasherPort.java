package com.novapay.accounts.application.port.out;

public interface PasswordHasherPort {

  String hash(String rawPassword);
}
