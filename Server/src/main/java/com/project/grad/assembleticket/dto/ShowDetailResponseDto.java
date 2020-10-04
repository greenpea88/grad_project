package com.project.grad.assembleticket.dto;

import com.project.grad.assembleticket.domain.entity.Shows;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ShowDetailResponseDto {

    private Shows show;
    private List<String> performers;

    public ShowDetailResponseDto(Shows show, List<String> performers){
        this.show = show;
        this.performers = performers;
    }

}
