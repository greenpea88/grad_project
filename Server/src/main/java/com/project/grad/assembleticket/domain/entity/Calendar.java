package com.project.grad.assembleticket.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id")
    private Shows show;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate calDate;

    @Column(columnDefinition = "TIME", nullable = false)
    private LocalTime calTime;

    @Column(length = 50, nullable = false)
    private String calTitle;

    @Column(length = 100)
    private String calMemo;

    @Column
    private boolean calDeleted;

    @Builder
    public Calendar(User user, Shows show, LocalDate cDate, LocalTime cTime, String cTitle, String cMemo){
        this.user = user;
        this.show = show;
        this.calDate = cDate;
        this.calTime = cTime;
        this.calTitle = cTitle;
        this.calMemo = cMemo;
        this.calDeleted = false;
    }

    public void update(LocalDate date, LocalTime time, String title, String memo){
        this.calDate = date;
        this.calTime = time;
        this.calTitle = title;
        this.calMemo = memo;
    }

    public void delete(){
        this.calDeleted = true;
    }

}
