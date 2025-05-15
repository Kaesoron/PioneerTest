package org.kaesoron.pioneer_app.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kaesoron.pioneer_app.entity.Account;
import org.kaesoron.pioneer_app.entity.User;
import org.kaesoron.pioneer_app.repository.AccountRepository;
import org.kaesoron.pioneer_app.repository.UserRepository;
import org.kaesoron.pioneer_app.service.AccountService;
import org.kaesoron.pioneer_app.service.impl.AccountServiceImpl;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

class TransferServiceTest {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountService accountService;

    @BeforeEach
    void setup() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountServiceImpl(accountRepository, userRepository);
    }

    @Test
    void testTransferFunds() {
        User sender = new User();
        sender.setId(1L);
        Account from = new Account(10L, sender, new BigDecimal("200.00"));

        User receiver = new User();
        receiver.setId(2L);
        Account to = new Account(11L, receiver, new BigDecimal("100.00"));

        sender.setAccount(from);
        receiver.setAccount(to);

        when(userRepository.findAllById(any())).thenReturn(List.of(sender, receiver));
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        accountService.transfer(1L, 2L, new BigDecimal("50.00"));

        assert from.getBalance().equals(new BigDecimal("150.00"));
        assert to.getBalance().equals(new BigDecimal("150.00"));
    }
}