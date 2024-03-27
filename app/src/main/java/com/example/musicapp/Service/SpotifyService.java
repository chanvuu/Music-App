package com.example.musicapp.Service;

import com.example.musicapp.Model.TokenResponse;

import kaaes.spotify.webapi.android.models.Album;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpotifyService {
    @FormUrlEncoded
    @POST("/api/token")
    Call<TokenResponse> getAccessToken(
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret
    );

    @GET("albums/{id}")
    Call<Album> getAlbum(@Path("id") String id, @Header("Authorization") String accessToken);
}

