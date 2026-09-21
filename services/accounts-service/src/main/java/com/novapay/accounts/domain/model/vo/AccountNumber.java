package com.novapay.accounts.domain.model.vo;

import java.util.regex.Pattern;

public record AccountNumber(String value) {

  private static final int BRANCH_LENGTH = 4;
  private static final int TOTAL_LENGTH = 12;
  private static final Pattern ONLY_DIGITS = Pattern.compile("\\d{12}");
  private static final Pattern NON_DIGITS = Pattern.compile("\\D");

  public AccountNumber {
    if (value == null) {
      throw new IllegalArgumentException("Numero da conta nao pode ser nulo");
    }
    if (!ONLY_DIGITS.matcher(value).matches()) {
      throw new IllegalArgumentException("Numero da conta deve conter 12 digitos: " + value);
    }
  }

  public static AccountNumber of(String rawValue){
    if (rawValue == null) {
      throw new IllegalArgumentException("Numero da conta não pode ser null");
    }
    return new AccountNumber(NON_DIGITS.matcher(rawValue).replaceAll(""));
  }

  public static AccountNumber of(String branch, String number) {
    return of (branch + number);
  }

  public String branch() {
    return value.substring(0, BRANCH_LENGTH);
  }

  public String number() {
    return value.substring(BRANCH_LENGTH);
  }

  public String formatted() {
    return branch() + "-" + number();
  }

  @Override
  public String toString() {
    return formatted();
  }
}
