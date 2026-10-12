package com.novapay.accounts.adapter.in.api;

import com.novapay.accounts.core.model.customer.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Clientes", description = "Cadastro de clientes")
@RequestMapping("/api/v1/customers")
public interface CustomerApi {

    @Operation(summary = "Cadastra cliente e abre conta")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode ="409", description = "CPF ou e-mail já cadastrado")
    @PostMapping
    ResponseEntity<RegisterCustomerResponse> register(@RequestBody RegisterCustomerRequest request);
}
