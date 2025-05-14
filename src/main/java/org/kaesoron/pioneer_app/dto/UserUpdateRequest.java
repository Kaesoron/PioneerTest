package org.kaesoron.pioneer_app.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private List<String> phones;  // может быть null или неполный список — валидация позже
    private List<String> emails;
}
