package com.novapay.accounts.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Email")
class EmailTest {

    @Test
    @DisplayName("deve aceitar email valido")
    void deveAceitarEmailValido() {
        assertThat(Email.of("maria@novapay.com").value())
                .isEqualTo("maria@novapay.com");
    }

    @Test
    @DisplayName("deve normalizar para minusculo")
    void deveNormalizarParaMinusculo() {
        assertThat(Email.of("Maria@NovaPay.COM").value())
                .isEqualTo("maria@novapay.com");
    }

    @Test
    @DisplayName("deve remover espacos nas pontas")
    void deveRemoverEspacosNasPontas() {
        assertThat(Email.of("  maria@novapay.com  ").value())
                .isEqualTo("maria@novapay.com");
    }

    @Test
    @DisplayName("deve tratar maiusculo e minusculo como iguais")
    void deveTratarMaiusculoEMinusculoComoIguais() {
        assertThat(Email.of("MARIA@NOVAPAY.COM"))
                .isEqualTo(Email.of("maria@novapay.com"));
    }

    @Test
    @DisplayName("deve expor o dominio")
    void deveExporODominio() {
        assertThat(Email.of("maria@novapay.com").domain()).isEqualTo("novapay.com");
    }

    @ParameterizedTest(name = "deve rejeitar \"{0}\"")
    @ValueSource(strings = {
            "", "   ", "maria", "maria@", "@novapay.com",
            "maria novapay.com", "maria@novapay", "maria@@novapay.com"
    })
    @DisplayName("deve rejeitar formato invalido")
    void deveRejeitarFormatoInvalido(String entrada) {
        assertThatThrownBy(() -> Email.of(entrada))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("deve rejeitar nulo")
    void deveRejeitarNulo() {
        assertThatThrownBy(() -> Email.of(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("deve mascarar preservando primeira letra e dominio")
    void deveMascararPreservandoPrimeiraLetraEDominio() {
        assertThat(Email.of("maria@novapay.com").masked())
                .isEqualTo("m****@novapay.com");
    }

    @Test
    @DisplayName("toString nao deve expor o email completo")
    void toStringNaoDeveExporEmailCompleto() {
        assertThat(Email.of("maria@novapay.com").toString())
                .isEqualTo("m****@novapay.com");
    }
}
