package com.project.grad.assembleticket.controller;

import com.project.grad.assembleticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/assemble-ticket")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestParam String idToken){
        userService.registerUser(idToken);
    }

}
