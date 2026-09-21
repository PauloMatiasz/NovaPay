package com.novapay.accounts.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AccountNumber")
class AccountNumberTest {

    @Test
    @DisplayName("deve criar a partir de agencia e numero")
    void deveCriarAPartirDeAgenciaENumero() {
        AccountNumber number = AccountNumber.of("0001", "00000042");

        assertThat(number.value()).isEqualTo("000100000042");
    }

    @Test
    @DisplayName("deve aceitar valor completo com hifen")
    void deveAceitarValorCompletoComHifen() {
        assertThat(AccountNumber.of("0001-00000042").value())
                .isEqualTo("000100000042");
    }

    @Test
    @DisplayName("deve expor agencia e numero separadamente")
    void deveExporAgenciaENumeroSeparadamente() {
        AccountNumber number = AccountNumber.of("000100000042");

        assertThat(number.branch()).isEqualTo("0001");
        assertThat(number.number()).isEqualTo("00000042");
    }

    @Test
    @DisplayName("deve formatar com hifen")
    void deveFormatarComHifen() {
        assertThat(AccountNumber.of("000100000042").formatted())
                .isEqualTo("0001-00000042");
    }

    @ParameterizedTest(name = "deve rejeitar \"{0}\"")
    @ValueSource(strings = {"", "123", "00010000004", "0001000000421", "0001-0000004A"})
    @DisplayName("deve rejeitar formato invalido")
    void deveRejeitarFormatoInvalido(String entrada) {
        assertThatThrownBy(() -> AccountNumber.of(entrada))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("deve rejeitar nulo")
    void deveRejeitarNulo() {
        assertThatThrownBy(() -> AccountNumber.of(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
