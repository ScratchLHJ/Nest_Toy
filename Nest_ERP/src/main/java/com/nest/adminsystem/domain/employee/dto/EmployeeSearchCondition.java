package com.nest.adminsystem.domain.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchCondition {

    private Long branchId;          // 지점 ID
    private String name;            // 직원 이름 검색
    private String employmentType;  // 고용형태 (정규직, 프리랜서 등)

    // 필요 시 입사일, 직급, 상태 등 조건 추가 가능
}
