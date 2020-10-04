package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.ShowPerformer;
import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.domain.repository.ShowPerformerRepository;
import com.project.grad.assembleticket.domain.repository.ShowRepository;
import com.project.grad.assembleticket.dto.ShowDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final ShowPerformerRepository showPerformerRepository;

    @Transactional
    public ShowDetailResponseDto getShowDetail(Long id){
        Shows show = showRepository.findById(id).orElse(null);
        List<ShowPerformer> showPerformer = showPerformerRepository.findAllByShowsId(id);
        List<String> performers = new ArrayList<>();
        for (ShowPerformer performer : showPerformer) {
            performers.add(performer.getPerformer().getName());
        }
        return new ShowDetailResponseDto(show, performers);
    }

    /**
     * 전체 공연 조회
     */
    @Transactional
    public List<Shows> findAllShows() {
        return showRepository.findAll();
    }

    /**
     * 카테고리별 공연 조회
     */
    @Transactional
    public List<Shows> findFilteredShow(int type) {
        return showRepository.findAllByType(type);
    }

    /**
     * 타이틀로 공연 조회
     */
    @Transactional
    public List<Shows> searchTitle(String title) {
        return showRepository.findAllByTitleContaining(title);
    }

}
