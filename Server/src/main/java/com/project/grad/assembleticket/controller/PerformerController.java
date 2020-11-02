package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.dto.PerformerDetailResponseDto;
import com.project.grad.assembleticket.service.PerformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/assemble-ticket")
@RestController
public class PerformerController {

    @Autowired
    private PerformerService performerService;

    @GetMapping("/performer")
    public PerformerDetailResponseDto getPerformerDetail(@RequestParam Long id){
        return performerService.getPerformerDetail(id);
    }

}
