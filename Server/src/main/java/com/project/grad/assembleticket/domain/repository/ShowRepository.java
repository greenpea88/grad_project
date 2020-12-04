package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.Shows;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Shows, Long> {

    List<Shows> findAllByTitleContaining(String keyword);
    List<Shows> findAllByRegisteredTimeLessThanEqual(Pageable pageable, LocalDateTime dateTime);
    List<Shows> findAllByTypeAndRegisteredTimeLessThanEqual(Pageable pageable, int type, LocalDateTime dateTime);
    List<Shows> findAllByRegisteredTimeAfterOrderByRegisteredTimeDesc(LocalDateTime dateTime);
    List<Shows> findAllByTypeAndRegisteredTimeAfterOrderByRegisteredTimeDesc(int type, LocalDateTime dateTime);

}
