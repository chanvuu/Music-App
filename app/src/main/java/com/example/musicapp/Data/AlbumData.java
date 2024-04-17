package com.example.musicapp.Data;

import androidx.annotation.NonNull;

import com.example.musicapp.Service.SpotifyService;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
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

    public void getSeveralAlbum(String accessToken, final OnAlbumLoadedListener listener) {
        Call<Album> call = service.getAlbum("Bearer " + accessToken);
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

    public void getNewReleases(String accessToken, int limit, final OnAlbumsLoadedListener listener) {
        // Gọi API để lấy danh sách các album mới phát hành
        Call<AlbumsPager> call = service.getNewReleases("Bearer " + accessToken, limit);
        call.enqueue(new Callback<AlbumsPager>() {
            @Override
            public void onResponse(@NonNull Call<AlbumsPager> call, @NonNull Response<AlbumsPager> response) {
                if (response.isSuccessful()) {
                    AlbumsPager albumsPager = response.body();
                    if (albumsPager != null) {
                        listener.onAlbumsLoaded(albumsPager.albums.items);
                    } else {
                        listener.onFailure("Response body is null");
                    }
                } else {
                    listener.onFailure("Failed to load new releases");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AlbumsPager> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public interface OnAlbumLoadedListener {
        void onAlbumLoaded(Album album);

        void onFailure(String errorMessage);
    }

    public interface OnAlbumsLoadedListener {
        void onAlbumsLoaded(List<AlbumSimple> albums);

        void onFailure(String errorMessage);
    }
}
