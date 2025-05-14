package org.kaesoron.pioneer_app.repository;

import org.kaesoron.pioneer_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailDataList_Email(String email);
    Optional<User> findByPhoneDataList_Phone(String phone);

    boolean existsByEmailDataList_Email(String email);
    boolean existsByPhoneDataList_Phone(String phone);

    Page<User> findAllByDateOfBirthAfter(LocalDate date, Pageable pageable);
    Page<User> findAllByNameStartingWithIgnoreCase(String name, Pageable pageable);
    Page<User> findAllByEmailDataList_Email(String email, Pageable pageable);
    Page<User> findAllByPhoneDataList_Phone(String phone, Pageable pageable);
}
