package com.deefy.group2.service;

import com.deefy.group2.dto.request.LoginRequest;
import com.deefy.group2.dto.response.LoginResponse;

public interface UserAuthenticationService {
    LoginResponse login(LoginRequest request);
}
