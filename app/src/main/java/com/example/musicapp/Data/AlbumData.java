package com.example.musicapp.Data;

import androidx.annotation.NonNull;

import com.example.musicapp.Service.SpotifyService;

import kaaes.spotify.webapi.android.models.Album;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumData {
    private static final String BASE_URL = "https://api.spotify.com/v1/";
    private final SpotifyService service;

    public AlbumData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SpotifyService.class);
    }

    public void getAlbum(String albumId, String accessToken, final OnAlbumLoadedListener listener) {
        Call<Album> call = service.getAlbum(albumId, "Bearer " + accessToken);
        call.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(@NonNull Call<Album> call, @NonNull Response<Album> response) {
                if (response.isSuccessful()) {
                    listener.onAlbumLoaded(response.body());
                } else {
                    listener.onFailure("Failed to load album");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Album> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public interface OnAlbumLoadedListener {
        void onAlbumLoaded(Album album);

        void onFailure(String errorMessage);
    }
}
