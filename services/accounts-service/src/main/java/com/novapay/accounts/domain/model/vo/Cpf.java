package com.novapay.accounts.domain.model.vo;

import java.util.regex.Pattern;

public record Cpf(String value) {

    private static final int LENGTH = 11;
    private static final int BASE_LENGTH = 9;
    private static final Pattern NON_DIGITS = Pattern.compile("\\D");
    private static final Pattern ALL_SAME_DIGIT = Pattern.compile("(\\d)\\1{10}");

    public Cpf {
        validate(value);
    }

    public static Cpf of(String rawValue) {
        if (rawValue == null) {
            throw new IllegalArgumentException("CPF nao pode ser nulo");
        }
        return new Cpf(onlyDigits(rawValue));
    }

    public String formatted() {
        return "%s.%s.%s-%s".formatted(
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6, 9),
                value.substring(9));
    }

    public String masked() {
        return "***.%s.%s-**".formatted(
                value.substring(3, 6),
                value.substring(6, 9));
    }

    @Override
    public String toString() {
        return masked();
    }

    private static void validate(String value) {
        if (value == null) {
            throw new IllegalArgumentException("CPF nao pode ser nulo");
        }
        if (value.length() != LENGTH) {
            throw new IllegalArgumentException("CPF deve conter 11 digitos");
        }
        if (ALL_SAME_DIGIT.matcher(value).matches()) {
            throw new IllegalArgumentException("CPF invalido");
        }
        if (hasInvalidCheckDigits(value)) {
            throw new IllegalArgumentException("CPF invalido");
        }
    }

    private static boolean hasInvalidCheckDigits(String value) {
        return calculateCheckDigit(value, BASE_LENGTH) != digitAt(value, 9)
                || calculateCheckDigit(value, BASE_LENGTH + 1) != digitAt(value, 10);
    }

    private static int calculateCheckDigit(String value, int digitsToUse) {
        int initialWeight = digitsToUse + 1;
        int sum = 0;
        for (int position = 0; position < digitsToUse; position++) {
            sum += digitAt(value, position) * (initialWeight - position);
        }
        int remainder = (sum * 10) % 11;
        return remainder == LENGTH - 1 ? 0 : remainder;
    }

    private static int digitAt(String value, int position) {
        return Character.getNumericValue(value.charAt(position));
    }

    private static String onlyDigits(String rawValue) {
        return NON_DIGITS.matcher(rawValue).replaceAll("");
    }
}
