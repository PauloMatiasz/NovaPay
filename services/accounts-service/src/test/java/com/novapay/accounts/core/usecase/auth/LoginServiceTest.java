package com.novapay.accounts.core.usecase.auth;

import com.novapay.accounts.core.exception.InvalidCredentialsException;
import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.port.CustomerRepositoryPort;
import com.novapay.accounts.core.port.LoginCommand;
import com.novapay.accounts.core.port.PasswordHasherPort;
import com.novapay.accounts.core.port.TokenGeneratorPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock private CustomerRepositoryPort customerRepositoryPort;
    @Mock private PasswordHasherPort passwordHasherPort;
    @Mock private TokenGeneratorPort tokenGeneratorPort;

    @InjectMocks
    private LoginService loginService;

    private final LoginCommand command = new LoginCommand("maria@email.com", "senha12345");

    @Test
    @DisplayName("Deve retornar token quando credenciais forem válidas")
    void deveRetornarTokenQuandoCredenciaisValidas() {

        Customer customer = mock(Customer.class);
        when(customer.passwordHash()).thenReturn("hash-fake");
        when(customerRepositoryPort.findByEmail(any())).thenReturn(Optional.of(customer));
        when(passwordHasherPort.matches("senha12345", "hash-fake")).thenReturn(true);
        when(tokenGeneratorPort.generate(customer)).thenReturn("token-fake");

        LoginResult result = loginService.login(command);

        assertThat(result.accessToken()).isEqualTo("token-fake");
    }

    @Test
    @DisplayName("Deve falhar quando e-mail não existir")
    void deveFalharQuandoEmailNaoExiste() {

        when(customerRepositoryPort.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(command))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(passwordHasherPort, never()).matches(any(), any());
        verify(tokenGeneratorPort, never()).generate(any());
    }

    @Test
    @DisplayName("Deve falhar quando a senha estiver errada")
    void deveFalharQuandoSenhaErrada() {

        Customer customer = mock(Customer.class);
        when(customer.passwordHash()).thenReturn("hash-fake");
        when(customerRepositoryPort.findByEmail(any())).thenReturn(Optional.of(customer));
        when(passwordHasherPort.matches("senha12345", "hash-fake")).thenReturn(false);

        assertThatThrownBy(() -> loginService.login(command))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(tokenGeneratorPort, never()).generate(any());
    }
}