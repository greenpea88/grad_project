package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.ShowPerformer;
import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.domain.repository.PerformerRepository;
import com.project.grad.assembleticket.domain.repository.ShowPerformerRepository;
import com.project.grad.assembleticket.dto.PerformerDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PerformerService {

    private final PerformerRepository performerRepository;
    private final ShowPerformerRepository showPerformerRepository;

    @Transactional
    public PerformerDetailResponseDto getPerformerDetail(Long id){
        Performer performer = performerRepository.findById(id).orElse(null);
        List<ShowPerformer> showPerformers = showPerformerRepository.findAllByPerformerId(id);
        List<Shows> shows = new ArrayList<>();
        for(ShowPerformer showPerformer : showPerformers) {
            shows.add(showPerformer.getShows());
        }
        return new PerformerDetailResponseDto(performer, shows);
    }

}
