package com.project.grad.assembleticket.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Performer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름
    @Column(length = 20, nullable = false, unique = true)
    private String name;

    // 사진
    @Column(length = 200)
    private String imgSrc;

    @Builder
    public Performer(String name, String imgSrc){
        this.name = name;
        this.imgSrc = imgSrc;
    }

}
