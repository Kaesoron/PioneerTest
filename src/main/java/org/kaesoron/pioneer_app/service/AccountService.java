package org.kaesoron.pioneer_app.service;

import java.math.BigDecimal;

public interface AccountService {
    void transfer(Long fromUserId, Long toUserId, BigDecimal amount);
    void applyInterestToAllAccounts();
}
