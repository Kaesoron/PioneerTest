package org.kaesoron.pioneer_app.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    private String name;
    private LocalDate dateOfBirth;
    private String password;
    private List<String> phones;
    private List<String> emails;
    private BigDecimal initialBalance;
}
