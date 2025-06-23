package com.nest.adminsystem.common.calendar.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class CalendarMemoResponseDTO {
    private Long memoId;
    private LocalDate memoDate;
    private String memoContent;
    private String writerName;
    private LocalDateTime createdAt;
}
