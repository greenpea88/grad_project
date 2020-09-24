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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    private String displayName;

    @Column(columnDefinition = "DATE")
    private LocalDate birthday;

    @Column(columnDefinition = "TINYINT")
    private int gender;

    @Builder
    public User(Long id, String email, String displayName, LocalDate birthday, int gender){
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.birthday = birthday;
        this.gender = gender;
    }

}
