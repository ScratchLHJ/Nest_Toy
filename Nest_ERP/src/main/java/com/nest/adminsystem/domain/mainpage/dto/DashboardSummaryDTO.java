package com.nest.adminsystem.domain.mainpage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDTO {
    private int employeeCount;
    private int branchCount;
    private long totalMonthlyPayroll;
    private long totalMonthlyRefund;
    private int newEmployeesThisMonth;
    private long avgPayrollPerEmployee;
    private Map<String, Long> payrollByBranch;
}
