package com.quickbite.gateway.Services;


import com.quickbite.gateway.Dto.JwtAuthenticationResponse;
import com.quickbite.gateway.Dto.RefreshTokenRequest;
import com.quickbite.gateway.Dto.SigninRequest;
import com.quickbite.gateway.Dto.SignupRequest;
import com.quickbite.gateway.Entities.User.User;

public interface AuthenticationService {

    User signup(SignupRequest signupRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
