package com.project.grad.assembleticket.dto;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.Shows;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PerformerDetailResponseDto {

    private Performer performer;
    private List<Shows> shows;

    public PerformerDetailResponseDto(Performer performer, List<Shows> shows){
        this.performer = performer;
        this.shows = shows;
    }

}
