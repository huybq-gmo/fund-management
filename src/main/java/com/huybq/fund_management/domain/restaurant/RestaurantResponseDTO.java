package com.huybq.fund_management.domain.restaurant;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String link;
    private int orderCount;
    private int totalVotes;
    private int totalStars;
    private Restaurant.RestaurantType type; // thêm type

}