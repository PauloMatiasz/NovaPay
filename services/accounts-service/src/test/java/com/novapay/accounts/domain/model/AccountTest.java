package com.novapay.accounts.domain.model;

import com.novapay.accounts.domain.exception.AccountNotActiveException;
import com.novapay.accounts.domain.exception.InsufficientBalanceException;
import com.novapay.accounts.domain.model.vo.AccountNumber;
import com.novapay.accounts.domain.model.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Account")
class AccountTest {

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final AccountNumber NUMBER = AccountNumber.of("0001", "00000042");

    private Account contaComSaldo(String saldo) {
        Account account = Account.open(CUSTOMER_ID, NUMBER);
        account.credit(Money.of(saldo));
        return account;
    }

    @Nested
    @DisplayName("Abertura")
    class Abertura {

        @Test
        @DisplayName("deve abrir conta com saldo zero e status ativo")
        void deveAbrirContaComSaldoZeroEAtiva() {
            Account account = Account.open(CUSTOMER_ID, NUMBER);

            assertThat(account.balance()).isEqualTo(Money.ZERO);
            assertThat(account.status()).isEqualTo(AccountStatus.ACTIVE);
            assertThat(account.id()).isNotNull();
            assertThat(account.customerId()).isEqualTo(CUSTOMER_ID);
        }

        @Test
        @DisplayName("deve gerar identificadores distintos para cada conta")
        void deveGerarIdentificadoresDistintos() {
            Account primeira = Account.open(CUSTOMER_ID, NUMBER);
            Account segunda = Account.open(CUSTOMER_ID, NUMBER);

            assertThat(primeira.id()).isNotEqualTo(segunda.id());
        }
    }

    @Nested
    @DisplayName("Credito")
    class Credito {

        @Test
        @DisplayName("deve somar ao saldo")
        void deveSomarAoSaldo() {
            Account account = contaComSaldo("100.00");

            account.credit(Money.of("50.00"));

            assertThat(account.balance()).isEqualTo(Money.of("150.00"));
        }

        @Test
        @DisplayName("deve rejeitar credito de valor zero")
        void deveRejeitarCreditoDeValorZero() {
            Account account = contaComSaldo("100.00");

            assertThatThrownBy(() -> account.credit(Money.ZERO))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("deve permitir credito em conta bloqueada")
        void devePermitirCreditoEmContaBloqueada() {
            Account account = contaComSaldo("100.00");
            account.block();

            account.credit(Money.of("50.00"));

            assertThat(account.balance()).isEqualTo(Money.of("150.00"));
        }

        @Test
        @DisplayName("deve rejeitar credito em conta encerrada")
        void deveRejeitarCreditoEmContaEncerrada() {
            Account account = Account.open(CUSTOMER_ID, NUMBER);
            account.close();

            assertThatThrownBy(() -> account.credit(Money.of("50.00")))
                    .isInstanceOf(AccountNotActiveException.class);
        }
    }

    @Nested
    @DisplayName("Debito")
    class Debito {

        @Test
        @DisplayName("deve subtrair do saldo")
        void deveSubtrairDoSaldo() {
            Account account = contaComSaldo("100.00");

            account.debit(Money.of("30.00"));

            assertThat(account.balance()).isEqualTo(Money.of("70.00"));
        }

        @Test
        @DisplayName("deve permitir debitar o saldo inteiro")
        void devePermitirDebitarSaldoInteiro() {
            Account account = contaComSaldo("100.00");

            account.debit(Money.of("100.00"));

            assertThat(account.balance()).isEqualTo(Money.ZERO);
        }

        @Test
        @DisplayName("deve rejeitar debito maior que o saldo")
        void deveRejeitarDebitoMaiorQueSaldo() {
            Account account = contaComSaldo("100.00");

            assertThatThrownBy(() -> account.debit(Money.of("100.01")))
                    .isInstanceOf(InsufficientBalanceException.class);
        }

        @Test
        @DisplayName("nao deve alterar o saldo quando o debito falha")
        void naoDeveAlterarSaldoQuandoDebitoFalha() {
            Account account = contaComSaldo("100.00");

            assertThatThrownBy(() -> account.debit(Money.of("500.00")))
                    .isInstanceOf(InsufficientBalanceException.class);

            assertThat(account.balance()).isEqualTo(Money.of("100.00"));
        }

        @Test
        @DisplayName("deve rejeitar debito em conta bloqueada")
        void deveRejeitarDebitoEmContaBloqueada() {
            Account account = contaComSaldo("100.00");
            account.block();

            assertThatThrownBy(() -> account.debit(Money.of("10.00")))
                    .isInstanceOf(AccountNotActiveException.class);
        }
    }

    @Nested
    @DisplayName("Ciclo de vida")
    class CicloDeVida {

        @Test
        @DisplayName("deve bloquear e desbloquear")
        void deveBloquearEDesbloquear() {
            Account account = Account.open(CUSTOMER_ID, NUMBER);

            account.block();
            assertThat(account.status()).isEqualTo(AccountStatus.BLOCKED);

            account.unblock();
            assertThat(account.status()).isEqualTo(AccountStatus.ACTIVE);
        }

        @Test
        @DisplayName("deve rejeitar encerramento com saldo")
        void deveRejeitarEncerramentoComSaldo() {
            Account account = contaComSaldo("0.01");

            assertThatThrownBy(account::close)
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("Identidade")
    class Identidade {

        @Test
        @DisplayName("contas sao iguais quando possuem o mesmo id")
        void contasSaoIguaisQuandoPossuemMesmoId() {
            Account account = contaComSaldo("100.00");
            Account mesmaConta = Account.restore(
                    account.id(), CUSTOMER_ID, NUMBER,
                    Money.of("999.99"), AccountStatus.BLOCKED);

            assertThat(account).isEqualTo(mesmaConta);
        }

        @Test
        @DisplayName("contas com ids diferentes nao sao iguais")
        void contasComIdsDiferentesNaoSaoIguais() {
            assertThat(Account.open(CUSTOMER_ID, NUMBER))
                    .isNotEqualTo(Account.open(CUSTOMER_ID, NUMBER));
        }
    }
}
