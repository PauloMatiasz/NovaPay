package com.novapay.accounts.domain.model.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

  private static final int SCALE = 2;
  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

  public static final Money ZERO = Money.of("0.00");

  public Money{
    Objects.requireNonNull(value, "Valor monetario não pode ser nulo");
    if (value.signum() < 0){
      throw new IllegalArgumentException("Valor monetario não pode ser negativo");
    }
    value = value.setScale(SCALE, ROUNDING_MODE);
  }

  public static Money of (BigDecimal value){
    if (value == null){
      throw new IllegalArgumentException("Valor monetario não poder ser nulo");
    }
    return new Money(value);
  }

  public static Money of(String value){
    return of(new BigDecimal(value));
  }

  public Money add(Money other){
    return new Money(this.value.add(other.value));
  }

  public Money subtract(Money other){
    return new Money(this.value.subtract(other.value));
  }

  public boolean isGreaterThan(Money other){
    return compareTo(other) > 0;
  }

  public boolean isLessThan(Money other){
    return compareTo(other) < 0;
  }

  public boolean isZero(){
    return compareTo(ZERO) == 0;
  }

  @Override
  public int compareTo(Money other){
    return this.value.compareTo(other.value);
  }

  @Override
  public String toString() {
    return value.toPlainString();
  }

}
