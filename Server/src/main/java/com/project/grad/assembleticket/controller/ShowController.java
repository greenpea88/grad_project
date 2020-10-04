package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.domain.entity.Shows;
import com.project.grad.assembleticket.dto.ShowDetailResponseDto;
import com.project.grad.assembleticket.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/assemble-ticket")
@RestController
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping("/show")
    public ShowDetailResponseDto getShowDetail(@RequestParam Long id){
        return showService.getShowDetail(id);
    }

    @GetMapping("/shows/all")
    public List<Shows> getAllShows(@RequestParam int page){
        return showService.getAllShows(page);
    }

    @GetMapping("/shows/type")
    public List<Shows> getTypeShows(@RequestParam int page, @RequestParam int type){
        return showService.getTypeShows(page, type);
    }

}
