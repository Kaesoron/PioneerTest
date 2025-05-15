package org.kaesoron.pioneer_app.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.kaesoron.pioneer_app.entity.Account;
import org.kaesoron.pioneer_app.entity.User;
import org.kaesoron.pioneer_app.repository.AccountRepository;
import org.kaesoron.pioneer_app.repository.UserRepository;
import org.kaesoron.pioneer_app.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public synchronized void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        log.info("Попытка перевода от {} к {} на сумму {}", fromUserId, toUserId, amount);

        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Cannot transfer to the same user");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        List<User> users = userRepository.findAllById(List.of(fromUserId, toUserId));
        if (users.size() != 2) {
            throw new RuntimeException("One or both users not found");
        }

        Account fromAccount = users.stream()
                .filter(u -> u.getId().equals(fromUserId))
                .findFirst()
                .map(User::getAccount)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAccount = users.stream()
                .filter(u -> u.getId().equals(toUserId))
                .findFirst()
                .map(User::getAccount)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        log.info("Баланс отправителя = {}", fromAccount.getBalance());
        log.info("Баланс получателя = {}", toAccount.getBalance());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Изменения балансов сохранены");
    }

    @Override
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void applyInterestToAllAccounts() {
        log.info("Запланированное увеличение баланса счетов");

        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            BigDecimal current = account.getBalance();

            BigDecimal maxBalance = current.multiply(BigDecimal.valueOf(2.07));
            if (current.compareTo(maxBalance) >= 0) {
                continue;
            }

            BigDecimal increased = current.multiply(BigDecimal.valueOf(1.10));
            if (increased.compareTo(maxBalance) > 0) {
                increased = maxBalance;
            }

            account.setBalance(increased);
        }

        accountRepository.saveAll(accounts);
    }
}
