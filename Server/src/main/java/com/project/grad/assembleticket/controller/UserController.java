package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.domain.entity.User;
import com.project.grad.assembleticket.dto.UserSaveRequestDto;
import com.project.grad.assembleticket.dto.UserUpdateRequestDto;
import com.project.grad.assembleticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/assemble-ticket")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public boolean login(@RequestParam String email){
        return userService.login(email);
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserSaveRequestDto requestDto){
        userService.registerUser(requestDto);
    }

    @GetMapping("/profile")
    public User getProfile(@RequestParam String email){
        return userService.getProfile(email);
    }

    @PutMapping("/profile")
    public User updateProfile(@RequestBody UserUpdateRequestDto requestDto){
        return userService.updateProfile(requestDto);
    }

}
