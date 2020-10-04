package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.domain.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShowService {

    private final ShowRepository showRepository;

    /**
     * 전체 공연 조회
     */
    public List<Shows> findAllShows() {
        return showRepository.findAll();
    }

    /**
     * 카테고리별 공연 조회
     */
    public List<Shows> findFilteredShow(int type) {
        return showRepository.findByCategory(type);
    }

    /**
     * 타이틀로 공연 조회
     */
    public List<Shows> searchTitle(String title) {
        return showRepository.findByTitleContaining(title);
    }

}
