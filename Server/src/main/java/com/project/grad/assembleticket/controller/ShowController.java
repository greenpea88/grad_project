package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.dto.ShowDetailResponseDto;
import com.project.grad.assembleticket.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/assemble-ticket")
@RestController
public class ShowController {

    @Autowired
    private ShowService showService;

    // 공연 상세 페이지
    @GetMapping("/show")
    public ShowDetailResponseDto getShowDetail(@RequestParam Long id){
        return showService.getShowDetail(id);
    }

    // 공연 목록 페이지 - 전체
    // time: 처음 0 page 요청할 때 현재 시간, 한 번 정해지면 매 요청마다 동일
    // page: 0부터 시작, 매 요청마다 1씩 증가
    @GetMapping("/shows/all")
    public List<Shows> getAllShows(@RequestParam int page, @RequestParam String time){
        return showService.getAllShows(page, LocalDateTime.parse(time));
    }

    // 공연 목록 페이지 - 타입별
    // time: 처음 0 page 요청할 때 현재 시간, 한 번 정해지면 매 요청마다 동일
    // page: 0부터 시작, 매 요청마다 1씩 증가
    // type: 뮤지컬 1, 콘서트 2, 연극 3, 클래식 4
    @GetMapping("/shows/type")
    public List<Shows> getTypeShows(@RequestParam int page, @RequestParam String time, @RequestParam int type){
        return showService.getTypeShows(page, LocalDateTime.parse(time), type);
    }

    // 공연 목록 페이지 - 전체 - 아래로 당기기
    // time: 처음 0 page 요청할 때 정해진 그 시간
    @GetMapping("/shows/all/new")
    public List<Shows> getNewAllShows(@RequestParam String time){
        return showService.getNewAllShows(LocalDateTime.parse(time));
    }

    // 공연 목록 페이지 - 타입별 - 아래로 당기기
    // time: 처음 0 page 요청할 때 정해진 그 시간
    // type: 뮤지컬 1, 콘서트 2, 연극 3, 클래식 4
    @GetMapping("/shows/type/new")
    public List<Shows> getNewTypeShows(@RequestParam String time, @RequestParam int type){
        return showService.getNewTypeShows(LocalDateTime.parse(time), type);
    }

}
