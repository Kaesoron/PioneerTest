package org.kaesoron.pioneer_app.controller;

import lombok.RequiredArgsConstructor;
import org.kaesoron.pioneer_app.dto.TransferRequestDto;
import org.kaesoron.pioneer_app.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final AccountService accountService;

    @PostMapping
    public void transfer(@AuthenticationPrincipal Jwt jwt,
                         @RequestBody TransferRequestDto request) {
        Long fromUserId = Long.parseLong(jwt.getSubject());
        accountService.transfer(fromUserId, request.getToUserId(), request.getAmount());
    }
}
