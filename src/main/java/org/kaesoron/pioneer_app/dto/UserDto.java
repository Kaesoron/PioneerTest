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
public class UserDto {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private List<String> phones;
    private List<String> emails;
    private BigDecimal balance;
}
