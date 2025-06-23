package com.nest.adminsystem.domain.employee.service;

import com.nest.adminsystem.domain.employee.dto.EmployeeDTO;
import com.nest.adminsystem.domain.employee.dto.EmployeeSearchCondition;
import com.nest.adminsystem.domain.employee.entity.Employee;
import com.nest.adminsystem.domain.employee.entity.EmployeeBranch;
import com.nest.adminsystem.domain.employee.repository.EmployeeBranchRepository;
import com.nest.adminsystem.domain.employee.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeBranchRepository employeeBranchRepository;

    @Override
    public List<EmployeeDTO> searchEmployees(EmployeeSearchCondition condition) {
        List<EmployeeBranch> employeeBranches;

        if (condition.getBranchId() != null) {
            employeeBranches = employeeBranchRepository.findByBranch_IdAndIsActive(condition.getBranchId(), "Y");
        } else {
            employeeBranches = employeeBranchRepository.findByIsActive("Y");
        }

        return employeeBranches.stream()
                .map(eb -> {
                    Employee emp = eb.getEmployee();
                    if (emp == null) throw new EntityNotFoundException("직원 정보 없음");
                    return new EmployeeDTO(
                            emp.getId(),
                            emp.getName(),
                            emp.getEmploymentType(),
                            emp.getPhone(),
                            eb.getBranch().getBranchName(),
                            eb.getPosition(),
                            eb.getStartDate(),
                            eb.getEndDate()
                    );
                })
                .filter(dto -> {
                    if (condition.getName() != null && !condition.getName().isBlank()) {
                        return dto.getName().contains(condition.getName());
                    }
                    return true;
                })
                .filter(dto -> {
                    if (condition.getEmploymentType() != null && !condition.getEmploymentType().isBlank()) {
                        return dto.getEmploymentType().equals(condition.getEmploymentType());
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}