package com.huybq.fund_management.domain.pen_bill;

import com.huybq.fund_management.domain.penalty.PenaltyMapper;
import com.huybq.fund_management.domain.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PenBillMapper {
    private final UserMapper userMapper;
    private final PenaltyMapper penaltyMapper;
    public PenBillDTO toDTO(PenBill penBill) {
        return PenBillDTO.builder()
                .id(penBill.getId())
                .userId(penBill.getUser().getId())
                .penaltySlug(penBill.getPenalty().getSlug())
                .dueDate(penBill.getDueDate())
                .description(penBill.getDescription())
                .amount(penBill.getTotalAmount())
                .paymentStatus(penBill.getPaymentStatus().name())
                .createdAt(penBill.getCreatedAt())
                .build();
    }

    public PenBillResponse toPenBillResponse(PenBill penBill) {
        return PenBillResponse.builder()
                .id(penBill.getId())
                .user(userMapper.toResponseDTO(penBill.getUser()))
                .penalty(penaltyMapper.toDTO(penBill.getPenalty()))
                .dueDate(penBill.getDueDate())
                .description(penBill.getDescription())
                .amount(penBill.getTotalAmount())
                .paymentStatus(penBill.getPaymentStatus())
                .createdAt(penBill.getCreatedAt())
                .build();
    }
}
