package com.line.service.spring.boot.linelogin.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class Utils {

    private static final SecureRandom RANDOM = new SecureRandom();

    private Utils() { };

    public  static String getToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return token;
    }

}
