package com.line.service.spring.boot.linelogin.api;


import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LineAPI {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("oauth/v2.1/token")
    Call<AccessToken> accessToken(

    )

}
