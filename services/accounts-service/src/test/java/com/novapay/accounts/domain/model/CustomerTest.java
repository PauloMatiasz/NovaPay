package com.novapay.accounts.domain.model;

import com.novapay.accounts.domain.model.vo.Cpf;
import com.novapay.accounts.domain.model.vo.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Customer")
class CustomerTest {

    private static final Cpf CPF = Cpf.of("52998224725");
    private static final Email EMAIL = Email.of("maria@novapay.com");
    private static final String HASH = "$2a$12$hashficticioparateste";

    private Customer clienteValido() {
        return Customer.register("Maria Silva", CPF, EMAIL, HASH);
    }

    @Nested
    @DisplayName("Cadastro")
    class Cadastro {

        @Test
        @DisplayName("deve cadastrar cliente ativo")
        void deveCadastrarClienteAtivo() {
            Customer customer = clienteValido();

            assertThat(customer.id()).isNotNull();
            assertThat(customer.fullName()).isEqualTo("Maria Silva");
            assertThat(customer.status()).isEqualTo(CustomerStatus.ACTIVE);
            assertThat(customer.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("deve remover espacos do nome")
        void deveRemoverEspacosDoNome() {
            Customer customer = Customer.register("  Maria Silva  ", CPF, EMAIL, HASH);

            assertThat(customer.fullName()).isEqualTo("Maria Silva");
        }

        @ParameterizedTest(name = "deve rejeitar nome \"{0}\"")
        @ValueSource(strings = {"", "   ", "Ma", "  Ma  "})
        @DisplayName("deve rejeitar nome curto")
        void deveRejeitarNomeCurto(String nome) {
            assertThatThrownBy(() -> Customer.register(nome, CPF, EMAIL, HASH))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("deve rejeitar nome nulo")
        void deveRejeitarNomeNulo() {
            assertThatThrownBy(() -> Customer.register(null, CPF, EMAIL, HASH))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("deve rejeitar nome com mais de 120 caracteres")
        void deveRejeitarNomeLongo() {
            assertThatThrownBy(() -> Customer.register("A".repeat(121), CPF, EMAIL, HASH))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("deve rejeitar hash em branco")
        void deveRejeitarHashEmBranco() {
            assertThatThrownBy(() -> Customer.register("Maria Silva", CPF, EMAIL, "   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("deve rejeitar cpf nulo")
        void deveRejeitarCpfNulo() {
            assertThatThrownBy(() -> Customer.register("Maria Silva", null, EMAIL, HASH))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Alteracoes")
    class Alteracoes {

        @Test
        @DisplayName("deve renomear")
        void deveRenomear() {
            Customer customer = clienteValido();

            customer.rename("Maria Souza");

            assertThat(customer.fullName()).isEqualTo("Maria Souza");
        }

        @Test
        @DisplayName("nao deve alterar o nome quando o rename falha")
        void naoDeveAlterarNomeQuandoRenameFalha() {
            Customer customer = clienteValido();

            assertThatThrownBy(() -> customer.rename("Ma"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThat(customer.fullName()).isEqualTo("Maria Silva");
        }

        @Test
        @DisplayName("deve trocar o email")
        void deveTrocarEmail() {
            Customer customer = clienteValido();
            Email novo = Email.of("maria.souza@novapay.com");

            customer.changeEmail(novo);

            assertThat(customer.email()).isEqualTo(novo);
        }

        @Test
        @DisplayName("deve rejeitar email nulo")
        void deveRejeitarEmailNulo() {
            Customer customer = clienteValido();

            assertThatThrownBy(() -> customer.changeEmail(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("deve trocar o hash da senha")
        void deveTrocarHash() {
            Customer customer = clienteValido();

            customer.changePasswordHash("$2a$12$novohash");

            assertThat(customer.passwordHash()).isEqualTo("$2a$12$novohash");
        }
    }

    @Nested
    @DisplayName("Status")
    class Status {

        @Test
        @DisplayName("deve desativar e reativar")
        void deveDesativarEReativar() {
            Customer customer = clienteValido();

            customer.deactivate();
            assertThat(customer.isActive()).isFalse();

            customer.activate();
            assertThat(customer.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("Identidade")
    class Identidade {

        @Test
        @DisplayName("clientes com mesmo id sao iguais")
        void clientesComMesmoIdSaoIguais() {
            Customer customer = clienteValido();
            Customer restaurado = Customer.restore(
                    customer.id(), "Outro Nome", CPF, EMAIL, HASH,
                    CustomerStatus.INACTIVE, Instant.now());

            assertThat(customer).isEqualTo(restaurado);
        }

        @Test
        @DisplayName("toString nao deve expor dados pessoais")
        void toStringNaoDeveExporDadosPessoais() {
            String texto = clienteValido().toString();

            assertThat(texto)
                    .doesNotContain("52998224725")
                    .doesNotContain("maria@novapay.com")
                    .doesNotContain("Maria Silva")
                    .doesNotContain(HASH);
        }
    }
}
