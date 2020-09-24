package com.project.grad.assembleticket.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 공연장 이름
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    // 공연장 위치
    @Column(length = 50)
    private String location;

    @Builder
    public Venue(String name, String location){
        this.name = name;
        this.location = location;
    }

}
