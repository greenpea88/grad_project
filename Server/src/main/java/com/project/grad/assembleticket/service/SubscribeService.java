package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.Performer;
import com.project.grad.assembleticket.domain.entity.Subscribe;
import com.project.grad.assembleticket.domain.entity.User;
import com.project.grad.assembleticket.domain.repository.PerformerRepository;
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

    // 구독 등록
    public Subscribe saveSubscribe(Long userId, Long performerId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        Performer performer = performerRepository.findById(performerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연자가 없습니다. id=" + performerId));
        return subscribeRepository.save(Subscribe.builder().user(user).performer(performer).build());
    }

    // 구독한 공연자 리스트
    public List<Performer> getSubscribedPerformers(Long id){
        List<Subscribe> subscribes = subscribeRepository.findAllByUserId(id);
        List<Performer> performers = new ArrayList<>();
        for (Subscribe subscribe : subscribes) {
            performers.add(subscribe.getPerformer());
        }
        return performers;
    }

}
