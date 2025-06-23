package com.nest.adminsystem.common.calendar.controller;

import com.nest.adminsystem.common.calendar.dto.*;
import com.nest.adminsystem.common.calendar.service.CalendarMemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar-memo")
@RequiredArgsConstructor
public class CalendarMemoRestController {

    private final CalendarMemoService calendarMemoService;

    // 🔍 자동완성용
    @GetMapping("/autocomplete")
    public List<CalendarMemoResponseDTO> autocomplete(@RequestParam String keyword) {
        return calendarMemoService.searchTopByContent(keyword);
    }

    // 📩 메모 저장 (신규)
    @PostMapping
    public void saveMemo(@RequestBody CalendarMemoSaveRequestDTO dto) {
        calendarMemoService.saveMemo(dto);
    }

    // 📝 메모 수정
    @PutMapping("/{id}")
    public void updateMemo(@PathVariable Long id,
                           @RequestBody CalendarMemoSaveRequestDTO dto) {
        calendarMemoService.updateMemo(id, dto);
    }

    // 📆 날짜별 조회
    @GetMapping("/date")
    public List<CalendarMemoResponseDTO> getByDate(@RequestParam("date")
                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return calendarMemoService.getMemosByDate(date);
    }

    // 🔎 검색 조건 조회
    @GetMapping("/range")
    public List<CalendarMemoResponseDTO> search(CalendarMemoSearchCondition cond) {
        return calendarMemoService.searchMemos(cond);
    }

    // ❌ 논리 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        calendarMemoService.deleteMemo(id);
    }

    //메모가 있는 날짜 달력에 표시
    @GetMapping("/event-markers")
    public List<Map<String, Object>> getEventMarkers(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return calendarMemoService.getMemoMarkersBetween(start, end);
    }


}
