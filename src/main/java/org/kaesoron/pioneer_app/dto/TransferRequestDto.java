package org.kaesoron.pioneer_app.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequestDto {
    private Long toUserId;
    private BigDecimal amount;
}
