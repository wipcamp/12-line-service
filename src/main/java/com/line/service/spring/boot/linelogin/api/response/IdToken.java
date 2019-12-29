package com.line.service.spring.boot.linelogin.api.response;

public class IdToken {

    public final String iss;
    public final String sub;
    public final String aud;
    public final Long exp;
    public final Long iat;
    public final String nonce;
    public final String name;
    public final String picture;
    public final String email;

    public IdToken(String iss, String sub, String aud, Long exp, Long iat, String nonce, String name, String picture, String email) {
        this.iss = iss;
        this.sub = sub;
        this.aud = aud;
        this.exp = exp;
        this.iat = iat;
        this.nonce = nonce;
        this.name = name;
        this.picture = picture;
        this.email = email;
    }
}
