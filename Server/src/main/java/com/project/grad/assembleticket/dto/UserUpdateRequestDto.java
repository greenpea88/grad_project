package com.project.grad.assembleticket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    private String email;
    private LocalDate birthday;
    private String gender;

    @Builder
    public UserUpdateRequestDto(String email, LocalDate birthday, String gender){
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
    }

}
