package org.kaesoron.pioneer_app.mapper;

import org.kaesoron.pioneer_app.dto.UserDto;
import org.kaesoron.pioneer_app.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .emails(user.getEmailDataList().stream()
                        .map(e -> e.getEmail()).collect(Collectors.toList()))
                .phones(user.getPhoneDataList().stream()
                        .map(p -> p.getPhone()).collect(Collectors.toList()))
                .balance(user.getAccount() != null ? user.getAccount().getBalance() : null)
                .build();
    }
}
