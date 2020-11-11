package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.ShowPerformer;
import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.domain.repository.ShowPerformerRepository;
import com.project.grad.assembleticket.domain.repository.ShowRepository;
import com.project.grad.assembleticket.dto.ShowDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final ShowPerformerRepository showPerformerRepository;

    // 공연 상세 페이지
    public ShowDetailResponseDto getShowDetail(Long id){
        Shows show = showRepository.findById(id).orElse(null);
        List<ShowPerformer> showPerformers = showPerformerRepository.findAllByShowsId(id);
        List<Performer> performers = new ArrayList<>();
        for (ShowPerformer showPerformer : showPerformers) {
            performers.add(showPerformer.getPerformer());
        }
        return new ShowDetailResponseDto(show, performers);
    }

    // 공연 목록 페이지 - 전체
    public List<Shows> getAllShows(int page, LocalDateTime dateTime) {
        // DB 저장 최신순, 공연 시작일 최근순
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.Direction.DESC, "registeredTime", "startDate");
        return showRepository.findAllByRegisteredTimeLessThanEqual(pageRequest, dateTime);
    }

    // 공연 목록 페이지 - 타입별
    public List<Shows> getTypeShows(int page, LocalDateTime dateTime, int type) {
        PageRequest pageRequest = PageRequest.of(page, 20, Sort.Direction.DESC, "registeredTime", "startDate");
        return showRepository.findAllByTypeAndRegisteredTimeLessThanEqual(pageRequest, type, dateTime);
    }

    // 공연 목록 페이지 - 전체 - 아래로 당기기
    public List<Shows> getNewAllShows(LocalDateTime dateTime){
        return showRepository.findAllByRegisteredTimeAfterOrderByRegisteredTimeDesc(dateTime);
    }

    // 공연 목록 페이지 - 타입별 - 아래로 당기기
    public List<Shows> getNewTypeShows(LocalDateTime dateTime, int type){
        return showRepository.findAllByTypeAndRegisteredTimeAfterOrderByRegisteredTimeDesc(type, dateTime);
    }

}
