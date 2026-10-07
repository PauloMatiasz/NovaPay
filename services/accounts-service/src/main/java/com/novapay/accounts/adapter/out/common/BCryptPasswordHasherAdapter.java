package com.novapay.accounts.adapter.out.common;

import com.novapay.accounts.core.port.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasherAdapter implements PasswordHasherPort {

    private static final int STRENGTH = 12;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(STRENGTH);

    @Override
    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
