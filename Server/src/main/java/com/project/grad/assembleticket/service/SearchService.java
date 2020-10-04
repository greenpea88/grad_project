package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.domain.repository.PerformerRepository;
import com.project.grad.assembleticket.dto.SearchResponseDto;
import com.project.grad.assembleticket.domain.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SearchService {

    private final ShowRepository showRepository;
    private final PerformerRepository performerRepository;

    public SearchResponseDto getSearchResult(String keyword) {
        List<Shows> showTitle = showRepository.findAllByTitleContaining(keyword);
        List<Performer> performerName = performerRepository.findAllByNameContaining(keyword);
        return new SearchResponseDto(showTitle, performerName);
    }
}
