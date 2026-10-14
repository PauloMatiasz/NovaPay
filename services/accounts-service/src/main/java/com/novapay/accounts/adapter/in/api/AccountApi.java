package com.novapay.accounts.adapter.in.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Contas", description = "Dados da conta do cliente logado")
@RequestMapping("/api/v1/accounts")
public interface AccountApi {

    @Operation(summary = "Retorna os dados e o saldo do cliente logado")
    @ApiResponse(responseCode = "200", description = "Dados da conta")
    @ApiResponse(responseCode = "401", description = "Token ausente, invalido ou expirado")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @GetMapping("/me")
    ResponseEntity<MyAccountResponse> getMe(@Parameter(hidden = true) Jwt jwt);
}
