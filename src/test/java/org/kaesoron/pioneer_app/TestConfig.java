package org.kaesoron.pioneer_app;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.boot.test.context.TestConfiguration;

import java.time.Instant;
import java.util.Map;

@TestConfiguration
@ConditionalOnMissingBean(JwtEncoder.class)
public class TestConfig {
    @Bean
    public JwtEncoder jwtEncoder() {
        return parameters -> new Jwt(
                "mock-token-value",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                Map.of("sub", "user123", "scope", "read write")
        );
    }
}
