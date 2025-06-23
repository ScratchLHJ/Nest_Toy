package com.nest.adminsystem.domain.employee.controller;

import com.nest.adminsystem.domain.employee.dto.EmployeeDTO;
import com.nest.adminsystem.domain.employee.dto.EmployeeSearchCondition;
import com.nest.adminsystem.domain.employee.entity.Branch;
import com.nest.adminsystem.domain.employee.repository.BranchRepository;
import com.nest.adminsystem.domain.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BranchRepository branchRepository;
    @GetMapping("/employee")
    public String showEmployeeList(@RequestParam(required = false) Long branchId,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String employmentType,
                                   Model model) {

        EmployeeSearchCondition condition = new EmployeeSearchCondition(branchId, name, employmentType);

        List<Branch> branches = branchRepository.findAll();
        List<EmployeeDTO> employeeList = employeeService.searchEmployees(condition);

        model.addAttribute("branches", branches);
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("title", "인사관리대장"); // layout에서 사용
        model.addAttribute("contentFragment", "employee/employeelist"); // layout에서 동적 삽입할 fragment 지정

        return "layout"; // layout.html 기준 템플릿 렌더링
    }

}
