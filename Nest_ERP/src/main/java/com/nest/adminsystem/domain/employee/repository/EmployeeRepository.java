package com.nest.adminsystem.domain.employee.repository;

import com.nest.adminsystem.domain.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // 필요 시 추가 쿼리 정의 가능
}
