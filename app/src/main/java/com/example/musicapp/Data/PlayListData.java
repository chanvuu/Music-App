package com.example.musicapp.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.musicapp.Model.EditPlayListRequest;
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

    public void updatePlaylist(String accessToken, String playlistId, EditPlayListRequest request) {
        Call<Void> call = service.updatePlaylist(accessToken, playlistId, request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("respone",response.message());
                Log.d("respone", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    // Xử lý khi yêu cầu thành công
                    Log.d("updatePlayList", "Successfully");
                } else {
                    // Xử lý khi yêu cầu thất bại
                    Log.d("updatePlayList", "Unsuccessfully");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình gửi yêu cầu
                Log.d("updatePlayList", "call api fail" + t.getMessage());
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
