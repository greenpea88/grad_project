package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.*;
import com.project.grad.assembleticket.domain.repository.PerformerRepository;
import com.project.grad.assembleticket.domain.repository.ShowPerformerRepository;
import com.project.grad.assembleticket.domain.repository.SubscribeRepository;
import com.project.grad.assembleticket.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserRepository userRepository;
    private final PerformerRepository performerRepository;
    private final ShowPerformerRepository showPerformerRepository;

    // 구독되어 있는 공연자인지 확인
    public boolean isSubscribed(String email, Long performerId){
        if(subscribeRepository.countByUserEmailAndPerformerId(email, performerId)==0) return false;
        else return true;
    }

    // 구독 등록
    public Subscribe saveSubscribe(String email, Long performerId){
        User user = userRepository.findByEmail(email);
        Performer performer = performerRepository.findById(performerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연자가 없습니다. id=" + performerId));
        return subscribeRepository.save(Subscribe.builder().user(user).performer(performer).build());
    }

    // 구독 해제
    public Long deleteSubscribe(String email, Long performerId){
        Subscribe subscribe = subscribeRepository.findByUserEmailAndPerformerId(email, performerId);
        subscribeRepository.delete(subscribe);
        return subscribe.getId();
    }

    // 공연자 리스트
    public List<Performer> getSubscribedPerformers(String email){
        List<Subscribe> subscribes = subscribeRepository.findAllByUserEmail(email);
        List<Performer> performers = new ArrayList<>();
        for (Subscribe subscribe : subscribes) {
            performers.add(subscribe.getPerformer());
        }
        return performers;
    }

    // 공연 리스트
    public List<Shows> getPerformerShows(String email, Long performerId){
        List<Performer> performers = getSubscribedPerformers(email);
        List<Shows> shows = new ArrayList<>();

        if(performerId != null){     // 특정 공연자의 공연만
            Performer performer = performerRepository.findById(performerId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 공연자가 없습니다. id=" + performerId));
            if(performers.contains(performer)){ // 구독한 공연자가 맞는지 확인
                List<ShowPerformer> showPerformers = showPerformerRepository.findAllByPerformerId(performerId);
                for (ShowPerformer showPerformer : showPerformers) {
                    shows.add(showPerformer.getShows());
                }
            }
            else throw new IllegalArgumentException("구독한 공연자가 아닙니다.");
        }
        else{   // 구독한 공연자들의 공연 전부
            List<Shows> showsTmp = new ArrayList<>();
            for (Performer performer : performers) {
                List<ShowPerformer> showPerformers = showPerformerRepository.findAllByPerformerId(performer.getId());
                for (ShowPerformer showPerformer : showPerformers) {
                    showsTmp.add(showPerformer.getShows());
                }
            }
            // 중복 제거
            for(int i=0; i<showsTmp.size(); i++){
                if(!shows.contains(showsTmp.get(i))){
                    shows.add(showsTmp.get(i));
                }
            }
        }

        return shows;
    }

}
