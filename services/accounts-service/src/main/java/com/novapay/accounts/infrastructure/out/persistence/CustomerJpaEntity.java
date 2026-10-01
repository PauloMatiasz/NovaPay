package com.novapay.accounts.infrastructure.out.persistence;

import java.time.Instant;
import java.util.UUID;

import com.novapay.accounts.domain.model.CustomerStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@Getter
@Setter

public class CustomerJpaEntity {

  @Id
  private UUID id;

  @Column(name = "full_name", nullable = false, length = 120)
  private String fullName;

  @Column(nullable = false, length = 11, unique = true)
  private String cpf;

  @Column(nullable = false, length = 254, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false, length = 100)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private CustomerStatus status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;
}
