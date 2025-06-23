package com.nest.adminsystem.common.calendar.service;

import com.nest.adminsystem.common.calendar.dto.*;
import com.nest.adminsystem.common.calendar.entity.CalendarMemo;
import com.nest.adminsystem.common.calendar.repository.CalendarMemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarMemoService {

    private final CalendarMemoRepository calendarMemoRepository;

    public void saveMemo(CalendarMemoSaveRequestDTO dto) {
        CalendarMemo memo = CalendarMemo.builder()
                .memoDate(dto.getMemoDate())
                .memoContent(dto.getMemoContent())
                .writerName(dto.getWriterName())
                .build();

        calendarMemoRepository.save(memo);
    }

    // ✨ 메모 수정
    public void updateMemo(Long memoId, CalendarMemoSaveRequestDTO dto) {
        calendarMemoRepository.findById(memoId).ifPresent(memo -> {
            memo.setMemoDate(dto.getMemoDate());
            memo.setMemoContent(dto.getMemoContent());
            memo.setWriterName(dto.getWriterName());
            calendarMemoRepository.save(memo);
        });
    }

    public List<CalendarMemoResponseDTO> getMemosByDate(LocalDate date) {
        return calendarMemoRepository.findByMemoDateAndIsDeleted(date, "N")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CalendarMemoResponseDTO> searchMemos(CalendarMemoSearchCondition cond) {
        return calendarMemoRepository
                .findByMemoDateBetweenAndWriterNameContainingIgnoreCaseAndMemoContentContainingIgnoreCaseAndIsDeletedOrderByMemoDateDesc(
                        cond.getFromDate(),
                        cond.getToDate(),
                        cond.getWriterName() != null ? cond.getWriterName() : "",
                        cond.getKeyword() != null ? cond.getKeyword() : "",
                        "N"
                )
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteMemo(Long memoId) {
        calendarMemoRepository.findById(memoId).ifPresent(memo -> {
            memo.setIsDeleted("Y");
            calendarMemoRepository.save(memo);
        });
    }

    public List<CalendarMemoResponseDTO> searchTopByContent(String keyword) {
        return calendarMemoRepository.findTop5ByMemoContentContainingAndIsDeletedOrderByMemoDateDesc(keyword, "N")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private CalendarMemoResponseDTO toDTO(CalendarMemo memo) {
        return CalendarMemoResponseDTO.builder()
                .memoId(memo.getMemoId())
                .memoDate(memo.getMemoDate())
                .memoContent(memo.getMemoContent())
                .writerName(memo.getWriterName())
                .createdAt(memo.getCreatedAt())
                .build();
    }

    public List<Map<String, Object>> getMemoMarkersBetween(LocalDate start, LocalDate end) {
        List<CalendarMemo> memoList = calendarMemoRepository
                .findByMemoDateBetweenAndIsDeleted(start, end, "N");

        Map<LocalDate, List<CalendarMemo>> grouped = memoList.stream()
                .collect(Collectors.groupingBy(CalendarMemo::getMemoDate));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<CalendarMemo>> entry : grouped.entrySet()) {
            Map<String, Object> marker = new HashMap<>();
            marker.put("title", "📝 " + entry.getValue().size() + "건");
            marker.put("start", entry.getKey().toString());
            marker.put("display", "background");  // 또는 'list-item'
            result.add(marker);
        }

        return result;
    }


}
