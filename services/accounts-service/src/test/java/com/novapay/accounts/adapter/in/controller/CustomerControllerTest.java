package com.novapay.accounts.adapter.in.controller;

import com.novapay.accounts.adapter.common.SecurityConfig;
import com.novapay.accounts.core.exception.CustomerAlreadyExistsException;
import com.novapay.accounts.core.port.RegisterCustomerUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private CustomerRestMapper mapper;

    @MockitoBean
    private RegisterCustomerUseCase useCase;

    @Test
    @DisplayName("Deve retornar 409 quando cliente já estiver cadastrado")
    void deveRetornar409QuandoClienteJaCadastrado() throws Exception {

        when(useCase.execute(any())).thenThrow(new CustomerAlreadyExistsException("Cliente já cadastrado"));

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
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Deve retornar 400 quando email vier vazio")
    void deveRetornar400QuandoEmailVierVazio() throws Exception {

        String body = """
        {
          "fullName": "Maria Silva",
          "cpf": "52998224725",
          "email": "",
          "password": "senha12345"
        }
        """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
