package com.novapay.accounts.adapter.out.common;

import com.novapay.accounts.core.model.customer.Customer;
import com.novapay.accounts.core.port.TokenGeneratorPort;              // ← novo
import org.springframework.beans.factory.annotation.Value;             // ← novo
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtTokenGeneratorAdapter implements TokenGeneratorPort {

    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;

    public JwtTokenGeneratorAdapter(JwtEncoder jwtEncoder,
                                    @Value("${novapay.jwt.expiration-minutes}") long expirationMinutes) {
        this.jwtEncoder = jwtEncoder;
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String generate(Customer customer) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("novapay-accounts")
                .subject(customer.id().toString())
                .issuedAt(now)
                .expiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}