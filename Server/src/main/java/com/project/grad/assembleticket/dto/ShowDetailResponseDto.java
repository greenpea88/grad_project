package com.project.grad.assembleticket.dto;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.Shows;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ShowDetailResponseDto {

    private Shows show;
    private List<Performer> performers;

    public ShowDetailResponseDto(Shows show, List<Performer> performers){
        this.show = show;
        this.performers = performers;
    }

}
