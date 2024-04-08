package com.example.musicapp.Data;

import androidx.annotation.NonNull;

import com.example.musicapp.Service.SpotifyService;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.UserPrivate;
import kaaes.spotify.webapi.android.models.UserPublic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserData {
    private static final String BASE_URL = "https://api.spotify.com/v1/";
    private final SpotifyService service;

    public UserData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SpotifyService.class);
    }

    public void getUser(String accessToken, final UserData.OnUserLoadedListener listener) {
        Call<UserPublic> call = service.getCurrentUser("Bearer " + accessToken);
        call.enqueue(new Callback<UserPublic>() {
            @Override
            public void onResponse(@NonNull Call<UserPublic> call, @NonNull Response<UserPublic> response) {
                if (response.isSuccessful()) {
                    listener.onUserLoaded(response.body());
                } else {
                    listener.onFailure("Failed to load user profile");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserPublic> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public interface OnUserLoadedListener {
        void onUserLoaded(UserPublic userPublic);

        void onFailure(String errorMessage);
    }
}
