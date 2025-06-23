package com.nest.adminsystem.domain.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "EMPLOYEE_BRANCH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_branch_seq")
    @SequenceGenerator(name = "employee_branch_seq", sequenceName = "seq_employee_branch", allocationSize = 1)
    @Column(name = "employee_branch_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "assignment_type")
    private String assignmentType;

    @Column(name = "position")
    private String position;

    @Column(name = "is_active", length = 1)
    private String isActive;  // 'Y' or 'N'

    @Column(name = "remark")
    private String remark;
}
