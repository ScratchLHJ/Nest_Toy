package com.nest.adminsystem.common.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_memo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calendarMemoSeq")
    @SequenceGenerator(name = "calendarMemoSeq", sequenceName = "seq_calendar_memo", allocationSize = 1)
    private Long memoId;

    private LocalDate memoDate;

    @Column(length = 1000)
    private String memoContent;

    private String writerName;

    private LocalDateTime createdAt;

    @Column(length = 1)
    private String isDeleted = "N";

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isDeleted = "N";
    }
}
