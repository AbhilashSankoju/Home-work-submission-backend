package com.portal.homework.service;

import com.portal.homework.dto.AuthResponse;
import com.portal.homework.dto.LoginRequest;
import com.portal.homework.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
