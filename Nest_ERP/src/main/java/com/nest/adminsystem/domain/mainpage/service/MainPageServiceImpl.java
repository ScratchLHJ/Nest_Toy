package com.nest.adminsystem.domain.mainpage.service;

import com.nest.adminsystem.domain.mainpage.dto.DashboardSummaryDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MainPageServiceImpl implements MainPageService {

    @Override
    public DashboardSummaryDTO getDashboardSummary() {
        // TODO: 실제 DB 연동 전 임시 데이터
        return DashboardSummaryDTO.builder()
                .employeeCount(18)
                .branchCount(3)
                .totalMonthlyPayroll(15_430_000L)
                .totalMonthlyRefund(275_000L)
                .newEmployeesThisMonth(2)
                .avgPayrollPerEmployee(857_000L)
                .payrollByBranch(
                        Map.of(
                                "죽전점", 8_000_000L,
                                "가락점", 5_000_000L,
                                "본사", 2_430_000L
                        )
                )
                .build();
    }
}
