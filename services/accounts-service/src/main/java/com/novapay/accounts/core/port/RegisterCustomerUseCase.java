package com.novapay.accounts.core.port;

public interface RegisterCustomerUseCase {

  RegisterCustomerResult execute(RegisterCustomerCommand command);
}
