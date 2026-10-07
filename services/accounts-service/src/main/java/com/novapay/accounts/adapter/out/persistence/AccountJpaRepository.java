package com.novapay.accounts.adapter.out.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, UUID>{

  @Query(value = "SELECT nextval('account_number_seq')", nativeQuery = true)
  Long nextAccountNumber();
}
