package com.novapay.accounts.integration;

import com.novapay.accounts.adapter.out.persistence.CustomerJpaEntity;
import com.novapay.accounts.adapter.out.persistence.CustomerJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CustomerRegistrationIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16.4"); // 6

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerJpaRepository customerRepository;

    @Test
    @DisplayName("Deve cadastrar cliente e recusar o mesmo CPF na segunda vez")
    void deveCadastrarERecusarDuplicado() throws Exception {
        String body = """
                {
                  "fullName": "Maria Silva",
                  "cpf": "52998224725",
                  "email": "maria@email.com",
                  "password": "senha12345"
                }
                """;

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").exists());

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve salvar senha criptografada")
    void deveSalvarSenhaCriptografada() throws Exception {
        String body = """
                {
                  "fullName": "João Souza",
                  "cpf": "11144477735",
                  "email": "joao@email.com",
                  "password": "senha12345"
                }
                """;

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.password").doesNotExist()); // 2

        CustomerJpaEntity salvo = customerRepository.findByCpf("11144477735").orElseThrow(); // 3

        assertThat(salvo.getPasswordHash())
                .isNotEqualTo("senha12345") // 4
                .startsWith("$2");          // 5
    }
}