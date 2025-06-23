package com.nest.adminsystem.domain.employee.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BRANCH")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
    @SequenceGenerator(name = "branch_seq", sequenceName = "seq_branch", allocationSize = 1)
    @Column(name = "branch_id")
    private Long id;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "deposit_account")
    private String depositAccount;

    @Column(name = "withdraw_account")
    private String withdrawAccount;

    @Column(name = "business_number", unique = true)
    private String businessNumber;
}
