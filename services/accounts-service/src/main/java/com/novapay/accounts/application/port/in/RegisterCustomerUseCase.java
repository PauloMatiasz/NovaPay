package com.novapay.accounts.application.port.in;

public interface RegisterCustomerUseCase {

  RegisterCustomerResult execute(RegisterCustomerCommand command);
}
