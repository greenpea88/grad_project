package com.project.grad.assembleticket.domain.repository;

import com.project.grad.assembleticket.domain.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findAllByUserId(Long id);

}
