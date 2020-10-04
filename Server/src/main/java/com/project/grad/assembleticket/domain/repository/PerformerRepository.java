package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.Performer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformerRepository extends JpaRepository<Performer, Long> {

    Performer findByName(String name);

}
