package org.kaesoron.pioneer_app.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateToken(Long userId) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("pioneer_app")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(String.valueOf(userId))
                .claims(claimsMap -> claimsMap.put("userId", userId))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Long getUserIdFromToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return Long.parseLong(jwt.getSubject());
    }

    public Long extractUserIdFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return getUserIdFromToken(token);
        }
        throw new RuntimeException("Token not found");
    }
}
