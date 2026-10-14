package com.novapay.accounts.adapter.in.controller;

import com.novapay.accounts.adapter.in.api.AuthApi;
import com.novapay.accounts.adapter.in.api.LoginRequest;
import com.novapay.accounts.adapter.in.api.LoginResponse;
import com.novapay.accounts.core.port.LoginCommand;
import com.novapay.accounts.core.port.LoginUseCase;
import com.novapay.accounts.core.usecase.auth.LoginResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(request.email(), request.password());
        LoginResult result = loginUseCase.login(command);
        return ResponseEntity.ok(new LoginResponse(result.accessToken()));
    }
}