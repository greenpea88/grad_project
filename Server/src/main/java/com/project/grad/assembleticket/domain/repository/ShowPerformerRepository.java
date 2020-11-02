package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.ShowPerformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowPerformerRepository extends JpaRepository<ShowPerformer, Long> {

    List<ShowPerformer> findAllByShowsId(Long id);
    List<ShowPerformer> findAllByPerformerId(Long id);

}
