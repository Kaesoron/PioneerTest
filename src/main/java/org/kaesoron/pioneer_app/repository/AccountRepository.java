package org.kaesoron.pioneer_app.repository;

import org.kaesoron.pioneer_app.entity.Account;
import org.kaesoron.pioneer_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser(User user);
}