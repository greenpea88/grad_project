package com.project.grad.assembleticket.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ShowPerformer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performer_id")
    private Performer performer;

    @Builder
    public ShowPerformer(Show show, Performer performer){
        this.show = show;
        this.performer = performer;
    }

}
