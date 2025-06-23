package com.nest.adminsystem.common.calendar.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarMemoSaveRequestDTO {
    private LocalDate memoDate;
    private String memoContent;
    private String writerName;
}
