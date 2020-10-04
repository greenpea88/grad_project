package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.Shows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Shows, Long> {
    Optional<Shows> findById(Long id);
    List<Shows> findByTitleContaining(String title);
    List<Shows> findByCategory(int type);
    List<Shows> findAll();
}
