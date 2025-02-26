package com.app.controller;

import com.app.dto.AuthLoginRequestDTO;
import com.app.dto.AuthResponseDTO;
import com.app.dto.RegisterUserDTO;
import com.app.service.UserDetailServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserDetailServiceImpl userDetailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO userRequest){
        return ResponseEntity.ok(this.userDetailService.loginUser(userRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterUserDTO> register(@RequestBody @Valid RegisterUserDTO userRequest) {
        return ResponseEntity.ok(this.userDetailService.signUpUser(userRequest));
    }
}
