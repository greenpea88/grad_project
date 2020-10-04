package com.project.grad.assembleticket.dto;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.Shows;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchResponseDto {

    private List<Shows> shows;
    private List<Performer> performers;

    public SearchResponseDto(List<Shows> shows, List<Performer> performers){
        this.shows = shows;
        this.performers = performers;
    }

}
