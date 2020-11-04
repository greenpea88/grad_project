package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findAllByUserIdAndCalDateAndCalDeletedFalse(Long id, LocalDate date);

}
