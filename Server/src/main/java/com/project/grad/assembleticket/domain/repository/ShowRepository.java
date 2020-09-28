package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    Optional<Show> findById(Long id);
    List<Show> findByTitleContaining(String title);
    List<Show> findByCategory(int type);
    List<Show> findAll();
}
