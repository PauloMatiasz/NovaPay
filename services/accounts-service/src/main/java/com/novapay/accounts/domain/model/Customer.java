package com.novapay.accounts.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.novapay.accounts.domain.model.vo.Cpf;
import com.novapay.accounts.domain.model.vo.Email;

public class Customer {

  private static final int MIN_NAME_LENGTH = 3;
  private static final int MAX_NAME_LENGTH = 120;

  private final UUID id;
  private String fullName;
  private final Cpf cpf;
  private Email email;
  private String passwordHash;
  private CustomerStatus status;
  private final Instant createdAt;

  private Customer(UUID id, String fullName, Cpf cpf, Email email,
                   String passwordHash, CustomerStatus status, Instant createdAt) {
    this.id = Objects.requireNonNull(id, "O id é obrigatório");
    this.fullName = validateName(fullName);
    this.cpf = Objects.requireNonNull(cpf, "O cpf é obrigatório");
    this.email = Objects.requireNonNull(email, "O email não pode ser nulo");
    this.passwordHash = validatePasswordHash(passwordHash);
    this.status = Objects.requireNonNull(status, "O status não pode ser nulo");
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt não pode ser nulo");
  }

  public static Customer register(String fullName, Cpf cpf, Email email, String passwordHash) {
    return new Customer(UUID.randomUUID(), fullName, cpf, email, passwordHash,
        CustomerStatus.ACTIVE, Instant.now());
  }

  public static Customer restore(UUID id, String fullName, Cpf cpf, Email email,
                                 String passwordHash, CustomerStatus status, Instant createdAt) {
    return new Customer(id, fullName, cpf, email, passwordHash, status, createdAt);
  }

  public void rename(String newName) {
    this.fullName = validateName(newName);
  }

  public void changeEmail(Email newEmail) {
    this.email = Objects.requireNonNull(newEmail, "O email não pode ser nulo");
  }

  public void changePasswordHash(String newHash) {
    this.passwordHash = validatePasswordHash(newHash);
  }

  public void deactivate() {
    this.status = CustomerStatus.INACTIVE;
  }

  public void activate() {
    this.status = CustomerStatus.ACTIVE;
  }

  public boolean isActive() {
    return status == CustomerStatus.ACTIVE;
  }

  public UUID id() {
    return id;
  }

  public String fullName() {
    return fullName;
  }

  public Cpf cpf() {
    return cpf;
  }

  public Email email() {
    return email;
  }

  public String passwordHash() {
    return passwordHash;
  }

  public CustomerStatus status() {
    return status;
  }

  public Instant createdAt() {
    return createdAt;
  }

  private static String validateName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Nome é obrigatório");
    }
    String trimmed = name.trim();
    if (trimmed.length() < MIN_NAME_LENGTH) {
      throw new IllegalArgumentException("Nome deve ter ao menos 3 caracteres");
    }
    if (trimmed.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException("Nome não pode ter mais que 120 caracteres");
    }
    return trimmed;
  }

  private static String validatePasswordHash(String hash) {
    if (hash == null || hash.isBlank()) {
      throw new IllegalArgumentException("Hash da senha é obrigatório");
    }
    return hash;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Customer customer)) {
      return false;
    }
    return id.equals(customer.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Customer[id=%s, cpf=%s]".formatted(id, cpf);
  }
}
