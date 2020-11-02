package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.repository.PerformerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PerformerService {

    private final PerformerRepository performerRepository;

    @Transactional
    public Performer getPerformerDetail(Long id){
        return performerRepository.findById(id).orElse(null);
    }

}
