package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.Performer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformerRepository extends JpaRepository<Performer, Long> {

    Performer findByName(String name);
    List<Performer> findAllByNameContaining(String keyword);

}
