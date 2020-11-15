package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.Calendar;
import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.domain.entity.User;
import com.project.grad.assembleticket.domain.repository.CalendarRepository;
import com.project.grad.assembleticket.domain.repository.ShowRepository;
import com.project.grad.assembleticket.domain.repository.UserRepository;
import com.project.grad.assembleticket.dto.CalendarSaveRequestDto;
import com.project.grad.assembleticket.dto.CalendarUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;

    // 해당 User 해당 Date의 일정 가져오기
    public List<Calendar> getCalendar(String email, LocalDate date){
        return calendarRepository.findAllByUserEmailAndCalDateAndCalDeletedFalse(email, date);
    }

    // 일정 등록하기
    public Calendar saveCalendar(CalendarSaveRequestDto requestDto){
        User user = userRepository.findByEmail(requestDto.getEmail());
        Shows show = showRepository.findById(requestDto.getShowId())
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 없습니다. id=" + requestDto.getShowId()));
        return calendarRepository.save(Calendar.builder().user(user).show(show)
                .cDate(requestDto.getCalDate()).cTime(requestDto.getCalTime())
                .cTitle(requestDto.getCalTitle()).cMemo(requestDto.getCalMemo())
                .alarmSet(requestDto.getAlarmSet()).build());
    }

    // 일정 수정하기
    public Calendar updateCalendar(Long id, CalendarUpdateRequestDto requestDto){
        Calendar calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 없습니다. id=" + id));
        calendar.update(requestDto.getCalDate(), requestDto.getCalTime(), requestDto.getCalTitle(),
                requestDto.getCalMemo(), requestDto.getAlarmSet());
        return calendar;
    }

    // 일정 삭제하기
    public Long deleteCalendar(Long id){
        Calendar calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 없습니다. id=" + id));
        calendar.delete();
        return calendar.getId();
    }

}
