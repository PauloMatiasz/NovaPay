package com.novapay.accounts.core.port;

public interface PasswordHasherPort {

  String hash(String rawPassword);
  boolean matches(String rawPassword, String hash);
}
