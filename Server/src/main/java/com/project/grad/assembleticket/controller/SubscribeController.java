package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.Subscribe;
import com.project.grad.assembleticket.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/assemble-ticket")
@RestController
public class SubscribeController {

    @Autowired
    private SubscribeService subscribeService;

    // 구독 등록
    @PostMapping("/subscribe")
    public Subscribe saveSubscribe(@RequestParam Long userId, @RequestParam Long performerId){
        return subscribeService.saveSubscribe(userId, performerId);
    }

    // 구독한 공연자 리스트
    @GetMapping("/subscribe/performers")
    public List<Performer> getSubscribedPerformers(@RequestParam Long userId){
        return subscribeService.getSubscribedPerformers(userId);
    }

}
