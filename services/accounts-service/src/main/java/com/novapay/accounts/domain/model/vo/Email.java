package com.novapay.accounts.domain.model.vo;

import java.util.regex.Pattern;

public record Email(String value) {

  private static final int MAX_LENGTH = 254;
  private static final char SEPARATOR = '@';
  private static final Pattern FORMAT = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

  public Email{
    if (value == null){
      throw new IllegalArgumentException("O email não pode ser nulo");
    }
    if (value.length() > MAX_LENGTH){
      throw new IllegalArgumentException("Email excede o tamanho maximo");
    }
    if (!FORMAT.matcher(value).matches()){
      throw new IllegalArgumentException("Email invalido:" +value);
    }

  }

  public static Email of(String rawValue){
    if (rawValue == null){
      throw new IllegalArgumentException("O email não pode ser nulo");
    }
    return new Email(rawValue.trim().toLowerCase());
  }

  public String localPart(){
    return value.substring(0, separatorIndex());
  }

  public String domain(){
    return value.substring(separatorIndex() + 1);
  }

  public String masked(){
    return localPart().charAt(0)+ "****" + SEPARATOR + domain();
  }

  @Override
  public String toString(){
    return masked();
  }

  private int separatorIndex(){
    return value.indexOf(SEPARATOR);
  }
}
