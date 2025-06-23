package com.nest.adminsystem.domain.employee.repository;

import com.nest.adminsystem.domain.employee.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
