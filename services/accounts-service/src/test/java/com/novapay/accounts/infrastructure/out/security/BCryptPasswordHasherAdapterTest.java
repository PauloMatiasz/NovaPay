package com.novapay.accounts.infrastructure.out.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.novapay.accounts.adapter.out.common.BCryptPasswordHasherAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BCryptPasswordHasherAdapterTest {

  private final BCryptPasswordHasherAdapter hasher = new BCryptPasswordHasherAdapter();

  @Test
  @DisplayName("O hash não é igual à senha")
  void hashNaoEIgualASenha() {
    String hash = hasher.hash("senhaForte123");

    assertThat(hash).isNotEqualTo("senhaForte123");
  }

  @Test
  @DisplayName("O hash começa com o prefixo do BCrypt com custo 12")
  void hashComecaComPrefixoBCrypt() {
    String hash = hasher.hash("senhaForte123");

    assertThat(hash).startsWith("$2a$12$");
  }

  @Test
  @DisplayName("A mesma senha gera hashes diferentes")
  void mesmaSenhaGeraHashesDiferentes() {
    String hash1 = hasher.hash("senhaForte123");
    String hash2 = hasher.hash("senhaForte123");

    assertThat(hash1).isNotEqualTo(hash2);
  }
}
