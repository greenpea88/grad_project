package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.domain.entity.Subscribe;
import com.project.grad.assembleticket.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/assemble-ticket")
@RestController
public class SubscribeController {

    @Autowired
    private SubscribeService subscribeService;

    // 구독되어 있는 공연자인지 확인
    @GetMapping("/subscribe")
    public boolean isSubscribed(@RequestParam String email, @RequestParam Long performerId){
        return subscribeService.isSubscribed(email, performerId);
    }

    // 구독 등록
    @PostMapping("/subscribe")
    public Subscribe saveSubscribe(@RequestParam String email, @RequestParam Long performerId){
        return subscribeService.saveSubscribe(email, performerId);
    }
    
    // 구독 해제
    @DeleteMapping("/subscribe")
    public Long deleteSubscribe(@RequestParam String email, @RequestParam Long performerId){
        return subscribeService.deleteSubscribe(email, performerId);
    }

    // 공연자 리스트
    @GetMapping("/subscribe/performers")
    public List<Performer> getSubscribedPerformers(@RequestParam String email){
        return subscribeService.getSubscribedPerformers(email);
    }
    
    // 공연 리스트
    @GetMapping("/subscribe/shows")
    public List<Shows> getPerformerShows(@RequestParam String email, @Nullable Long performerId){
        return subscribeService.getPerformerShows(email, performerId);
    }
}
