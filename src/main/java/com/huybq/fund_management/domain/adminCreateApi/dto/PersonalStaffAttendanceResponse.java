package com.huybq.fund_management.domain.adminCreateApi.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonalStaffAttendanceResponse {
    private List<LeaveRequestDto> items;
    private Integer total;
    private Integer page;
}
