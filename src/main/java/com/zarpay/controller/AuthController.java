package com.zarpay.controller;

import com.zarpay.dto.SignupRequest;
import com.zarpay.dto.SignupResponse;
import com.zarpay.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody @Valid SignupRequest request) {
        return authService.signup(request);
    }
}
