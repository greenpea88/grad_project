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
    @GetMapping("/shows/all")
    public List<Shows> getAllShows(@RequestParam int page){
        return showService.getAllShows(page);
    }

    // 공연 목록 페이지 - 타입별
    @GetMapping("/shows/type")
    public List<Shows> getTypeShows(@RequestParam int page, @RequestParam int type){
        return showService.getTypeShows(page, type);
    }

    // 공연 목록 페이지 - 전체 - 아래로 당기기
    @GetMapping("/shows/all/new")
    public List<Shows> getNewAllShows(@RequestParam String date){
        return showService.getNewAllShows(LocalDateTime.parse(date));
    }

    // 공연 목록 페이지 - 타입별 - 아래로 당기기
    @GetMapping("/shows/type/new")
    public List<Shows> getNewTypeShows(@RequestParam String date, @RequestParam int type){
        return showService.getNewTypeShows(LocalDateTime.parse(date), type);
    }

}
