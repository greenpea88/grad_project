package com.project.grad.assembleticket.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 20)
    private String displayName;

    @Column(columnDefinition = "DATE")
    private LocalDate birthday;

    @Column(columnDefinition = "TINYINT")
    private int gender;

    @Builder
    public User(String email, String displayName){
        this.email = email;
        this.displayName = displayName;
    }

    public void update(LocalDate birthday, int gender){
        this.birthday = birthday;
        this.gender = gender;
    }

}
