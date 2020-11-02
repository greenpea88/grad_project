package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.Calendar;
import com.project.grad.assembleticket.domain.repository.CalendarRepository;
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

    public List<Calendar> getCalendar(Long id, LocalDate date){
        return calendarRepository.findAllByUserIdAndCalDate(id, date);
    }

}
