package com.project.grad.assembleticket.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Shows {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 공연 제목
    @Column(length = 100, unique = true)
    private String title;

    // 공연 종류
    @Column(nullable = false)
    private int type;

    // 첫공 날짜
    @Column(/*columnDefinition = "DATE", */nullable = false)
    private LocalDate startDate;

    // 막공 날짜
    @Column(/*columnDefinition = "DATE"*/)
    private LocalDate endDate;

    // 티켓오픈 일시
    @Column(/*columnDefinition = "DATETIME"*/)
    private LocalDateTime ticketOpen;

    // 공연 시간 관련 정보
    @Column(length = 200)
    private String time;

    // 러닝타임
    @Column
    private int runningTime;

    // 티켓 가격 관련 정보
    @Column(length = 200)
    private String price;

    // 티켓 구매처
    @Column(length = 50)
    private String buyTicket;

    // 포스터 이미지
    @Column(length = 200, nullable = false)
    private String posterSrc;

    // 공연장
    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Builder
    public Shows(String title, int type, LocalDate startDate, LocalDate endDate, LocalDateTime ticketOpen,
                String time, int runningTime, String price, String buyTicket, String posterSrc, Venue venue){
        this.title = title;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ticketOpen = ticketOpen;
        this.time = time;
        this.runningTime = runningTime;
        this.price = price;
        this.buyTicket = buyTicket;
        this.posterSrc = posterSrc;
        this.venue = venue;
    }

}
