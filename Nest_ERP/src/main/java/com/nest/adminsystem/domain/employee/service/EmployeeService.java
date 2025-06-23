package com.nest.adminsystem.domain.employee.service;

import com.nest.adminsystem.domain.employee.dto.EmployeeDTO;
import com.nest.adminsystem.domain.employee.dto.EmployeeSearchCondition;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> searchEmployees(EmployeeSearchCondition condition);

    // 향후 개별 조회/수정/삭제용 메서드 추가 예정
}
