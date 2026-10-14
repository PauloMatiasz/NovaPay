package com.novapay.accounts.adapter.in.controller;

import com.novapay.accounts.adapter.in.api.AccountApi;
import com.novapay.accounts.adapter.in.api.MyAccountResponse;
import com.novapay.accounts.core.port.GetMyAccountUseCase;
import com.novapay.accounts.core.port.MyAccountResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AccountController implements AccountApi {

    private final GetMyAccountUseCase getMyAccountUseCase;

    public AccountController(GetMyAccountUseCase getMyAccountUseCase) {
        this.getMyAccountUseCase = getMyAccountUseCase;
    }

    @Override
    public ResponseEntity<MyAccountResponse> getMe(@AuthenticationPrincipal Jwt jwt) {
        UUID customerId = UUID.fromString(jwt.getSubject());
        MyAccountResult result = getMyAccountUseCase.getMyAccount(customerId);
        return ResponseEntity.ok(new MyAccountResponse(
                result.fullName(),
                result.maskedCpf(),
                result.email(),
                result.accountNumber(),
                result.balance(),
                result.status()
        ));
    }
}
