package com.novapay.accounts.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, UUID>{

  boolean existsByCpf(String cpf);
  Optional<CustomerJpaEntity> findByCpf(String cpf);
  boolean existsByEmail(String email);
}
