package com.project.grad.assembleticket.service;

import com.project.grad.assembleticket.domain.entity.User;
import com.project.grad.assembleticket.domain.repository.UserRepository;
import com.project.grad.assembleticket.dto.UserSaveRequestDto;
import com.project.grad.assembleticket.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void registerUser(UserSaveRequestDto requestDto){
        // DB에 이미 존재하는 User인지 확인 → DB에 없는 경우 User 정보 저장
        if(userRepository.countByEmail(requestDto.getEmail())==0){
            userRepository.save(User.builder().displayName(requestDto.getDisplayName()).email(requestDto.getEmail()).build());
        }
    }

    public User getProfile(String email){
        return userRepository.findByEmail(email);
    }

    public User updateProfile(UserUpdateRequestDto requestDto){
        User user = userRepository.findByEmail(requestDto.getEmail());
        user.update(requestDto.getBirthday(), requestDto.getGender());
        return user;
    }

}
