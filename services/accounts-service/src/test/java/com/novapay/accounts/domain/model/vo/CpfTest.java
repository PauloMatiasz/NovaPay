package com.novapay.accounts.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Cpf")
class CpfTest {

    private static final String CPF_VALIDO = "52998224725";
    private static final String CPF_VALIDO_FORMATADO = "529.982.247-25";

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("deve aceitar cpf valido sem formatacao")
        void deveAceitarCpfValidoSemFormatacao() {
            assertThat(Cpf.of(CPF_VALIDO).value()).isEqualTo(CPF_VALIDO);
        }

        @Test
        @DisplayName("deve aceitar cpf valido com pontuacao")
        void deveAceitarCpfValidoComPontuacao() {
            assertThat(Cpf.of(CPF_VALIDO_FORMATADO).value()).isEqualTo(CPF_VALIDO);
        }

        @Test
        @DisplayName("deve remover espacos em branco")
        void deveRemoverEspacos() {
            assertThat(Cpf.of(" 529 982 247 25 ").value()).isEqualTo(CPF_VALIDO);
        }

        @Test
        @DisplayName("deve tratar formatado e nao formatado como iguais")
        void deveTratarFormatadoENaoFormatadoComoIguais() {
            assertThat(Cpf.of(CPF_VALIDO_FORMATADO)).isEqualTo(Cpf.of(CPF_VALIDO));
        }
    }

    @Nested
    @DisplayName("Rejeicao")
    class Rejeicao {

        @Test
        @DisplayName("deve rejeitar nulo")
        void deveRejeitarNulo() {
            assertThatThrownBy(() -> Cpf.of(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nulo");
        }

        @ParameterizedTest(name = "deve rejeitar \"{0}\"")
        @ValueSource(strings = {"", "   ", "123", "5299822472", "529982247251"})
        @DisplayName("deve rejeitar quantidade incorreta de digitos")
        void deveRejeitarQuantidadeIncorretaDeDigitos(String entrada) {
            assertThatThrownBy(() -> Cpf.of(entrada))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("11 digitos");
        }

        @ParameterizedTest(name = "deve rejeitar sequencia {0}")
        @ValueSource(strings = {
                "00000000000", "11111111111", "22222222222", "33333333333",
                "44444444444", "55555555555", "66666666666", "77777777777",
                "88888888888", "99999999999"
        })
        @DisplayName("deve rejeitar digitos repetidos")
        void deveRejeitarDigitosRepetidos(String entrada) {
            assertThatThrownBy(() -> Cpf.of(entrada))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("invalido");
        }

        @ParameterizedTest(name = "deve rejeitar {0}")
        @ValueSource(strings = {"52998224726", "52998224715", "11144477736"})
        @DisplayName("deve rejeitar digito verificador incorreto")
        void deveRejeitarDigitoVerificadorIncorreto(String entrada) {
            assertThatThrownBy(() -> Cpf.of(entrada))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("invalido");
        }
    }

    @Nested
    @DisplayName("Apresentacao")
    class Apresentacao {

        @Test
        @DisplayName("deve formatar com pontuacao")
        void deveFormatarComPontuacao() {
            assertThat(Cpf.of(CPF_VALIDO).formatted()).isEqualTo(CPF_VALIDO_FORMATADO);
        }

        @Test
        @DisplayName("deve mascarar ocultando inicio e fim")
        void deveMascararOcultandoInicioEFim() {
            assertThat(Cpf.of(CPF_VALIDO).masked()).isEqualTo("***.982.247-**");
        }

        @Test
        @DisplayName("toString nao deve expor o cpf completo")
        void toStringNaoDeveExporCpfCompleto() {
            String texto = Cpf.of(CPF_VALIDO).toString();

            assertThat(texto).doesNotContain(CPF_VALIDO);
            assertThat(texto).isEqualTo("***.982.247-**");
        }
    }

    @Nested
    @DisplayName("Algoritmo")
    class Algoritmo {

        @ParameterizedTest(name = "deve aceitar {0}")
        @ValueSource(strings = {"52998224725", "11144477735", "12345678909"})
        @DisplayName("deve aceitar cpfs validos conhecidos")
        void deveAceitarCpfsValidosConhecidos(String entrada) {
            assertThat(Cpf.of(entrada)).isNotNull();
        }
    }
}
