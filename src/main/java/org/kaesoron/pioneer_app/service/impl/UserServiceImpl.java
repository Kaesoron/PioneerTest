package org.kaesoron.pioneer_app.service.impl;

import org.kaesoron.pioneer_app.dto.*;
import org.kaesoron.pioneer_app.entity.EmailData;
import org.kaesoron.pioneer_app.entity.PhoneData;
import org.kaesoron.pioneer_app.entity.User;
import org.kaesoron.pioneer_app.mapper.UserMapper;
import org.kaesoron.pioneer_app.repository.EmailDataRepository;
import org.kaesoron.pioneer_app.repository.PhoneDataRepository;
import org.kaesoron.pioneer_app.repository.UserRepository;
import org.kaesoron.pioneer_app.security.JwtService;
import org.kaesoron.pioneer_app.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhoneDataRepository phoneRepo;
    private final EmailDataRepository emailRepo;
    private final JwtService jwtService;

    @Override
    @Cacheable("currentUser")
    public UserDto getCurrentUser() {
        Long userId = jwtService.getCurrentUserId();
        return userRepository.findById(userId)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Cacheable("searchUsers")
    public List<UserDto> searchUsers(SearchFilterDto filter) {
        var pageable = PageRequest.of(filter.getPage(), filter.getSize());

        if (filter.getPhone() != null) {
            return userRepository.findAllByPhoneDataList_Phone(filter.getPhone(), pageable)
                    .stream().map(UserMapper::toDto).toList();
        } else if (filter.getEmail() != null) {
            return userRepository.findAllByEmailDataList_Email(filter.getEmail(), pageable)
                    .stream().map(UserMapper::toDto).toList();
        } else if (filter.getDateOfBirth() != null) {
            LocalDate date = LocalDate.parse(filter.getDateOfBirth());
            return userRepository.findAllByDateOfBirthAfter(date, pageable)
                    .stream().map(UserMapper::toDto).toList();
        } else if (filter.getName() != null) {
            return userRepository.findAllByNameStartingWithIgnoreCase(filter.getName(), pageable)
                    .stream().map(UserMapper::toDto).toList();
        } else {
            return userRepository.findAll(pageable)
                    .stream().map(UserMapper::toDto).toList();
        }
    }

    @Override
    @Transactional
    public void updateCurrentUser(UserUpdateRequest request) {
        Long userId = jwtService.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getEmails() != null) {
            var newEmails = request.getEmails().stream()
                    .filter(email -> !emailRepo.existsByEmail(email))
                    .map(email -> EmailData.builder().email(email).user(user).build())
                    .toList();

            user.getEmailDataList().clear();
            user.getEmailDataList().addAll(newEmails);
        }

        if (request.getPhones() != null) {
            var newPhones = request.getPhones().stream()
                    .filter(phone -> !phoneRepo.existsByPhone(phone))
                    .map(phone -> PhoneData.builder().phone(phone).user(user).build())
                    .toList();

            user.getPhoneDataList().clear();
            user.getPhoneDataList().addAll(newPhones);
        }

        userRepository.save(user);
    }
}
