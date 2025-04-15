package com.project.simple_order.controllers;

import com.project.simple_order.service.UserService;
import com.project.simple_order.utils.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/my-info")
    public ResponseEntity<AppResponse> getUserInfoAndOrderHistory(){
        return ResponseEntity.ok(userService.getUserDetailsWithOrderHistory());
    }
}
