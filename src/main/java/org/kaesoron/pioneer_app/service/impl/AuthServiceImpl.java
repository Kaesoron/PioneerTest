package org.kaesoron.pioneer_app.service.impl;

import org.kaesoron.pioneer_app.dto.AuthRequestDto;
import org.kaesoron.pioneer_app.dto.JwtResponseDto;
import org.kaesoron.pioneer_app.entity.User;
import org.kaesoron.pioneer_app.repository.UserRepository;
import org.kaesoron.pioneer_app.security.JwtService;
import org.kaesoron.pioneer_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponseDto authenticate(AuthRequestDto request) {
        User user = userRepository.findByEmailDataList_Email(request.getEmailOrPhone())
                .or(() -> userRepository.findByPhoneDataList_Phone(request.getEmailOrPhone()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getId());
        return new JwtResponseDto(token);
    }
}