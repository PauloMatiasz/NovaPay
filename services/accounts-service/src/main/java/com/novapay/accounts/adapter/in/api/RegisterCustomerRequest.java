package com.novapay.accounts.adapter.in.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record RegisterCustomerRequest(

  @NotBlank
  @Size(min = 3, max = 120)
  String fullName,

  @NotBlank
  @CPF String cpf,

  @NotBlank
  @Email
  String email,

  @NotBlank
  @Size(min = 8)
  String password
){
  
}
