package com.example.musicapp.Data;

import androidx.annotation.NonNull;

import com.example.musicapp.Model.FeaturedPlaylists;
import com.example.musicapp.Model.PlaylistBase;
import com.example.musicapp.Model.PlaylistSimple;
import com.example.musicapp.Service.SpotifyService;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.UserPublic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlayListData {
    private static final String BASE_URL = "https://api.spotify.com/v1/";
    private final SpotifyService service;

    public PlayListData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SpotifyService.class);
    }

    public void getFeaturedPlaylists(String accessToken,int limit, int offset, String locale, final PlayListData.OnPlayListLoadedListener listener) {
        Call<FeaturedPlaylists> call = service.getFeaturedPlaylists("Bearer " + accessToken, limit, offset, locale);
        call.enqueue(new Callback<FeaturedPlaylists>() {
            @Override
            public void onResponse(@NonNull Call<FeaturedPlaylists> call, @NonNull Response<FeaturedPlaylists> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    listener.onPlayListLoaded(response.body().playlists.items);

                } else {
                    listener.onFailure("Failed to load play list");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FeaturedPlaylists> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void getPlaylist(String playlistId, String accessToken, final OnPlaylistLoadedListener1 listener) {
        Call<Playlist> call = service.getPlayList(playlistId, "Bearer " + accessToken);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
                if (response.isSuccessful()) {
                    listener.onPlaylistLoaded(response.body());
                } else {
                    listener.onFailure("Failed to load playlist");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public interface OnPlayListLoadedListener {
        void onPlayListLoaded(List<PlaylistSimple> featuredPlaylists);

        void onFailure(String errorMessage);
    }
    public interface OnPlaylistLoadedListener1 {
        void onPlaylistLoaded(Playlist playlist);

        void onFailure(String errorMessage);
    }


}
