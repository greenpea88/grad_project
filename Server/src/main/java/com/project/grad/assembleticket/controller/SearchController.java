package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.dto.SearchResponseDto;
import com.project.grad.assembleticket.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/assemble-ticket")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public SearchResponseDto getSearchResult(@RequestParam String keyword){
        return searchService.getSearchResult(keyword);
    }

}
