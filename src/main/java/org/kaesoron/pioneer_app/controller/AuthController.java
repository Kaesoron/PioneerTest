package org.kaesoron.pioneer_app.controller;

import lombok.RequiredArgsConstructor;
import org.kaesoron.pioneer_app.dto.AuthRequestDto;
import org.kaesoron.pioneer_app.dto.JwtResponseDto;
import org.kaesoron.pioneer_app.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody AuthRequestDto request) {
        return authService.authenticate(request);
    }
}
