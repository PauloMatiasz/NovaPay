package com.novapay.accounts.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Money")
class MoneyTest {

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("deve normalizar para duas casas decimais")
        void deveNormalizarParaDuasCasas() {
            Money money = Money.of("10.5");

            assertThat(money.value()).isEqualByComparingTo("10.50");
            assertThat(money.value().scale()).isEqualTo(2);
        }

        @Test
        @DisplayName("deve arredondar usando half even")
        void deveArredondarUsandoHalfEven() {
            assertThat(Money.of("10.125").value()).isEqualByComparingTo("10.12");
            assertThat(Money.of("10.135").value()).isEqualByComparingTo("10.14");
        }

        @Test
        @DisplayName("deve rejeitar valor nulo")
        void deveRejeitarValorNulo() {
            assertThatThrownBy(() -> Money.of((BigDecimal) null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nulo");
        }

        @Test
        @DisplayName("deve rejeitar valor negativo")
        void deveRejeitarValorNegativo() {
            assertThatThrownBy(() -> Money.of("-0.01"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("negativo");
        }
    }

    @Nested
    @DisplayName("Operacoes")
    class Operacoes {

        @Test
        @DisplayName("deve somar dois valores")
        void deveSomar() {
            assertThat(Money.of("10.50").add(Money.of("5.25")))
                    .isEqualTo(Money.of("15.75"));
        }

        @Test
        @DisplayName("deve subtrair dois valores")
        void deveSubtrair() {
            assertThat(Money.of("10.50").subtract(Money.of("0.50")))
                    .isEqualTo(Money.of("10.00"));
        }

        @Test
        @DisplayName("deve falhar quando a subtracao resulta em negativo")
        void deveFalharQuandoSubtracaoFicaNegativa() {
            assertThatThrownBy(() -> Money.of("10.00").subtract(Money.of("10.01")))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("nao deve alterar o objeto original ao somar")
        void naoDeveAlterarOriginal() {
            Money original = Money.of("100.00");

            original.add(Money.of("50.00"));

            assertThat(original).isEqualTo(Money.of("100.00"));
        }
    }

    @Nested
    @DisplayName("Comparacao")
    class Comparacao {

        @Test
        @DisplayName("deve identificar valor maior")
        void deveIdentificarMaior() {
            assertThat(Money.of("10.01").isGreaterThan(Money.of("10.00"))).isTrue();
            assertThat(Money.of("10.00").isGreaterThan(Money.of("10.00"))).isFalse();
        }

        @Test
        @DisplayName("deve considerar iguais valores com escalas diferentes")
        void deveConsiderarIguaisComEscalasDiferentes() {
            assertThat(Money.of("10.0")).isEqualTo(Money.of("10.00"));
        }
    }
}
