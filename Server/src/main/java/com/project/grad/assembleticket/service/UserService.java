package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.User;
import com.project.grad.assembleticket.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void registerUser(String idTokenString){
        //TODO: 구글로그인 토큰 받아 와서 사용자 등록하기
    }

    @Transactional
    public User getProfile(String email){
        return userRepository.findByEmail(email);
    }

}
