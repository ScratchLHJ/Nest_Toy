package com.nest.adminsystem.domain.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long employeeId;
    private String name;
    private String employmentType;
    private String phone;
    private String branchName;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
}
