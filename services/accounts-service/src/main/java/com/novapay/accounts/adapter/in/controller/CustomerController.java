package com.novapay.accounts.adapter.in.controller;

import com.novapay.accounts.adapter.in.api.CustomerApi;
import com.novapay.accounts.adapter.in.api.RegisterCustomerRequest;
import com.novapay.accounts.adapter.in.api.RegisterCustomerResponse;
import com.novapay.accounts.core.port.RegisterCustomerResult;
import com.novapay.accounts.core.port.RegisterCustomerUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class CustomerController implements CustomerApi {

    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final CustomerRestMapper mapper;

    public CustomerController(RegisterCustomerUseCase registerCustomerUseCase,
                              CustomerRestMapper mapper) {
        this.registerCustomerUseCase = registerCustomerUseCase;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<RegisterCustomerResponse> register(
            @Valid @RequestBody RegisterCustomerRequest request) {

        RegisterCustomerResult result = registerCustomerUseCase.execute(mapper.toCommand(request));
        RegisterCustomerResponse response = mapper.toResponse(result);

        URI location = URI.create("/api/v1/customers/" + result.customerId());
        return ResponseEntity.created(location).body(response);
    }
}