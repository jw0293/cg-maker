package com.html.cgmaker.login.domain.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConstants {

    public static final String AUTH_HEADER = "AccessToken";
    public static final String REFRESH_HEADER = "RefreshToken";
    public static final String TOKEN_TYPE = "BEARER";
}
