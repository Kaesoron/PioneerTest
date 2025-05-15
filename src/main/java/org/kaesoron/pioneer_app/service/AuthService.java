package org.kaesoron.pioneer_app.service;

import org.kaesoron.pioneer_app.dto.AuthRequestDto;
import org.kaesoron.pioneer_app.dto.JwtResponseDto;

public interface AuthService {
    JwtResponseDto authenticate(AuthRequestDto request);
}