package com.project.grad.assembleticket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    private String email;
    private String displayName;

    @Builder
    public UserSaveRequestDto(String email, String displayName){
        this.email = email;
        this.displayName = displayName;
    }

}
