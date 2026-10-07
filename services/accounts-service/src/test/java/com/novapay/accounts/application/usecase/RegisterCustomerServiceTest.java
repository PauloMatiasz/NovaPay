package com.novapay.accounts.application.usecase;

import com.novapay.accounts.core.port.RegisterCustomerCommand;
import com.novapay.accounts.core.port.RegisterCustomerResult;
import com.novapay.accounts.core.port.AccountNumberGeneratorPort;
import com.novapay.accounts.core.port.AccountRepositoryPort;
import com.novapay.accounts.core.port.CustomerRepositoryPort;
import com.novapay.accounts.core.port.PasswordHasherPort;
import com.novapay.accounts.core.exception.CustomerAlreadyExistsException;
import com.novapay.accounts.core.model.account.Account;
import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.model.account.AccountNumber;
import com.novapay.accounts.core.model.account.Money;
import com.novapay.accounts.core.usecase.customer.RegisterCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterCustomerServiceTest {

    private static final String NOME = "Maria Silva";
    private static final String CPF = "529.982.247-25";
    private static final String EMAIL = "maria@novapay.com";
    private static final String SENHA = "senhaForte123";
    private static final String HASH = "hash-gerado";

    @Mock private CustomerRepositoryPort customerRepository;
    @Mock private AccountRepositoryPort accountRepository;
    @Mock private PasswordHasherPort passwordHasher;
    @Mock private AccountNumberGeneratorPort accountNumberGenerator;

    private RegisterCustomerService service;

    @BeforeEach
    void setUp() {
        service = new RegisterCustomerService(
                customerRepository, accountRepository, passwordHasher, accountNumberGenerator);
    }

    private RegisterCustomerCommand comandoComSenha(String senha) {
        return new RegisterCustomerCommand(NOME, CPF, EMAIL, senha);
    }

    private void configurarCaminhoFeliz() {
        when(customerRepository.existsByCpf(any())).thenReturn(false);
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(passwordHasher.hash(SENHA)).thenReturn(HASH);
        when(accountNumberGenerator.next()).thenReturn(AccountNumber.of("0001", "00000042"));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    }

    // ===== senha =====

    @Test
    @DisplayName("deve rejeitar senha curta sem consultar nada")
    void deveRejeitarSenhaCurta() {
        assertThatThrownBy(() -> service.execute(comandoComSenha("1234567")))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(customerRepository, passwordHasher);
    }

    @Test
    @DisplayName("deve rejeitar senha nula sem consultar nada")
    void deveRejeitarSenhaNula() {
        assertThatThrownBy(() -> service.execute(comandoComSenha(null)))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(customerRepository, passwordHasher);
    }

    // ===== duplicidade =====

    @Test
    @DisplayName("deve rejeitar cpf ja cadastrado")
    void deveRejeitarCpfDuplicado() {
        when(customerRepository.existsByCpf(any())).thenReturn(true);

        assertThatThrownBy(() -> service.execute(comandoComSenha(SENHA)))
                .isInstanceOf(CustomerAlreadyExistsException.class);

        verify(customerRepository, never()).save(any());
        verifyNoInteractions(accountRepository, passwordHasher, accountNumberGenerator);
    }

    @Test
    @DisplayName("deve rejeitar email ja cadastrado")
    void deveRejeitarEmailDuplicado() {
        when(customerRepository.existsByCpf(any())).thenReturn(false);
        when(customerRepository.existsByEmail(any())).thenReturn(true);

        assertThatThrownBy(() -> service.execute(comandoComSenha(SENHA)))
                .isInstanceOf(CustomerAlreadyExistsException.class);

        verify(customerRepository, never()).save(any());
        verifyNoInteractions(accountRepository, passwordHasher, accountNumberGenerator);
    }


    @Test
    @DisplayName("deve cadastrar cliente e abrir conta")
    void deveCadastrarClienteEAbrirConta() {
        configurarCaminhoFeliz();

        RegisterCustomerResult result = service.execute(comandoComSenha(SENHA));

        assertThat(result.customerId()).isNotNull();
        assertThat(result.accountId()).isNotNull();
        assertThat(result.accountNumber()).isEqualTo("0001-00000042");
    }

    @Test
    @DisplayName("deve salvar o hash e nunca a senha pura")
    void deveSalvarHashENuncaSenhaPura() {
        configurarCaminhoFeliz();
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        service.execute(comandoComSenha(SENHA));

        verify(customerRepository).save(captor.capture());
        assertThat(captor.getValue().passwordHash()).isEqualTo(HASH).isNotEqualTo(SENHA);
    }

    @Test
    @DisplayName("deve abrir conta com saldo zero ligada ao cliente")
    void deveAbrirContaLigadaAoCliente() {
        configurarCaminhoFeliz();
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        RegisterCustomerResult result = service.execute(comandoComSenha(SENHA));

        verify(accountRepository).save(captor.capture());
        assertThat(captor.getValue().customerId()).isEqualTo(result.customerId());
        assertThat(captor.getValue().balance()).isEqualTo(Money.ZERO);
    }
}
