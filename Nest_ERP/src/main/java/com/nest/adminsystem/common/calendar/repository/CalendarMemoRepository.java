package com.nest.adminsystem.common.calendar.repository;

import com.nest.adminsystem.common.calendar.entity.CalendarMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarMemoRepository extends JpaRepository<CalendarMemo, Long> {

    List<CalendarMemo> findByMemoDateAndIsDeleted(LocalDate memoDate, String isDeleted);

    List<CalendarMemo> findByMemoDateBetweenAndIsDeletedOrderByMemoDateDesc(
            LocalDate from, LocalDate to, String isDeleted
    );

    List<CalendarMemo> findByMemoDateBetweenAndWriterNameContainingIgnoreCaseAndMemoContentContainingIgnoreCaseAndIsDeletedOrderByMemoDateDesc(
            LocalDate from, LocalDate to, String writerName, String keyword, String isDeleted
    );

    List<CalendarMemo> findTop5ByMemoContentContainingAndIsDeletedOrderByMemoDateDesc(String keyword, String isDeleted);

    List<CalendarMemo> findByMemoDateBetweenAndIsDeleted(LocalDate start, LocalDate end, String isDeleted);


}
