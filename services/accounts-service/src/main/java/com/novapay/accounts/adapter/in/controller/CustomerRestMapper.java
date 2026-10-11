package com.novapay.accounts.adapter.in.controller;

import com.novapay.accounts.adapter.in.api.RegisterCustomerRequest;
import com.novapay.accounts.adapter.in.api.RegisterCustomerResponse;
import com.novapay.accounts.core.port.RegisterCustomerCommand;
import com.novapay.accounts.core.port.RegisterCustomerResult;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CustomerRestMapper {

    RegisterCustomerCommand toCommand(RegisterCustomerRequest request);

    RegisterCustomerResponse toResponse(RegisterCustomerResult result);
}