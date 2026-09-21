package com.novapay.accounts.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("AccountStatus")
class AccountStatusTest {

  @Test
  @DisplayName("Somente conta ativa permite debito")
  void somenteContaAtivaPermiteDebito(){
    assertThat(AccountStatus.ACTIVE.allowsDebit()).isTrue();
    assertThat(AccountStatus.BLOCKED.allowsDebit()).isFalse();
    assertThat(AccountStatus.CLOSED.allowsDebit()).isFalse();
  }

  @Test
  @DisplayName("Conta bloqueada ainda pode receber credito")
  void contaBloqueadaPodeReceberCredito(){
    assertThat(AccountStatus.ACTIVE.allowsCredit()).isTrue();
    assertThat(AccountStatus.BLOCKED.allowsCredit()).isTrue();
    assertThat(AccountStatus.CLOSED.allowsCredit()).isFalse();
  }

  @Test
  @DisplayName("isActive deve refletir apenas o status ativo")
  void isActiveDeveRefletirApenasOStatusAtivo(){
    assertThat(AccountStatus.ACTIVE.isActive()).isTrue();
    assertThat(AccountStatus.BLOCKED.isActive()).isFalse();
  }
  
}
