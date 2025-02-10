package com.ehub.service;

import com.ehub.dto.request.SignInRequest;
import com.ehub.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(String refreshToken);
}
