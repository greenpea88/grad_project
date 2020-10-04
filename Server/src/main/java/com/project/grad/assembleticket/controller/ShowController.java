package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.dto.ShowDetailResponseDto;
import com.project.grad.assembleticket.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/assemble-ticket")
@RestController
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping("/show")
    public ShowDetailResponseDto getShowDetail(@RequestParam Long id){
        return showService.getShowDetail(id);
    }

}
