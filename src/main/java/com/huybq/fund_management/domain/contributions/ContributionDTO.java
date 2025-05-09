package com.huybq.fund_management.domain.contributions;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributionDTO {

    private Long userId;

    @NotNull(message = "periodId is required")
    private Long periodId;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be positive")
    private BigDecimal totalAmount;
    private String note;
    private String fundType;

    private Set<Long> userIds;
}
