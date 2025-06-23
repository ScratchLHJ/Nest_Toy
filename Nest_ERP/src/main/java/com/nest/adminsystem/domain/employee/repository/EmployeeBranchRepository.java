package com.nest.adminsystem.domain.employee.repository;

import com.nest.adminsystem.domain.employee.entity.EmployeeBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeBranchRepository extends JpaRepository<EmployeeBranch, Long> {

    List<EmployeeBranch> findByBranch_IdAndIsActive(Long branchId, String isActive);

    List<EmployeeBranch> findByIsActive(String isActive);
}
