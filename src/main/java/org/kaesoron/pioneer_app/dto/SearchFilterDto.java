package org.kaesoron.pioneer_app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchFilterDto {
    private String name;
    private String email;
    private String phone;
    private String dateOfBirth; // формат: yyyy-MM-dd
    private int page;
    private int size;
}