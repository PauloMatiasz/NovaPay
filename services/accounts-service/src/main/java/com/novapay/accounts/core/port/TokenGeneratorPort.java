package com.novapay.accounts.core.port;

import com.novapay.accounts.core.model.customer.Customer;

public interface TokenGeneratorPort {

    String generate(Customer customer);
}
