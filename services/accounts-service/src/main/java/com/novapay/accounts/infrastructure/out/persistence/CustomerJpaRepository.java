package com.novapay.accounts.infrastructure.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, UUID>{

  boolean existsByCpf(String cpf);

  boolean existsByEmail(String email);
}
