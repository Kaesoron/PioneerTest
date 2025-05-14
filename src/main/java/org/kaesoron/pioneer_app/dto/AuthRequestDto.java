package org.kaesoron.pioneer_app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequestDto {
    private String emailOrPhone;
    private String password;
}
