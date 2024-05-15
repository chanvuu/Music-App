package com.example.musicapp.Data;

import android.util.Log;

import androidx.annotation.NonNull;


import com.example.musicapp.Model.AddTrackRequest;

import com.example.musicapp.Model.EditPlayListRequest;
import com.example.musicapp.Model.FeaturedPlaylists;
import com.example.musicapp.Model.PlaylistBase;
import com.example.musicapp.Model.PlaylistBody;
import com.example.musicapp.Model.PlaylistSimple;
import com.example.musicapp.Model.RemoveTracksRequest;
import com.example.musicapp.Model.UserPlayList;
import com.example.musicapp.Service.SpotifyService;
import com.google.firebase.database.DatabaseReference;

import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.models.Playlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlayListData {
    private static final String BASE_URL = "https://api.spotify.com/v1/";
    private final SpotifyService service;
    private DatabaseReference playlistsRef;

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
                    Log.d("aaaaaaaaaaa", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }


    public void getUserCurrentPlayList(String accessToken, int limit, int offset, final OnUserCurrentPlaylistLoadedListener listener) {
        Call<UserPlayList> call = service.getCurrentUserPlaylists("Bearer " + accessToken, limit, offset);
        call.enqueue(new Callback<UserPlayList>() {
            @Override
            public void onResponse(@NonNull Call<UserPlayList> call, @NonNull Response<UserPlayList> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<UserPlayList> playLists = Collections.singletonList(response.body()); // Chuyển TracksPager thành danh sách
                    listener.onUserCurrentPlaylistLoaded(playLists);
                } else {
                    listener.onFailure("Failed to load play list");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserPlayList> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
    public void createPlayList(String accessToken, String userId, PlaylistBody playlistBody) {
        Call<PlaylistBase> call = service.createPlaylists("Bearer " + accessToken, userId, playlistBody);
        call.enqueue(new Callback<PlaylistBase>() {
            @Override
            public void onResponse(@NonNull Call<PlaylistBase> call, @NonNull Response<PlaylistBase> response) {

                if (response.isSuccessful()) {
                    Log.d("createPlayList", "successfully");
                    assert response.body() != null;
                    List<PlaylistBase> playLists = Collections.singletonList(response.body()); // Chuyển TracksPager thành danh sách
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaylistBase> call, @NonNull Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
    }

    public void checkUsersSavedTracks(String accessToken, String trackIds, OnTrackSavedStatusListener listener) {
        Call<Boolean[]> call = service.checkUsersSavedTracks("Bearer " + accessToken, trackIds);

        call.enqueue(new Callback<Boolean[]>() {
            @Override
            public void onResponse(Call<Boolean[]> call, Response<Boolean[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean[] savedTracks = response.body();
                    for (int i = 0; i < savedTracks.length; i++) {
                        Log.d("Liked tracks", "Track ID: " + trackIds.split(",")[i] + " is saved: " + savedTracks[i]);
                        // Gửi trạng thái của track thông qua listener
                        listener.onTrackSavedStatusReceived(savedTracks[i]);
                    }
                } else {
                    Log.e("Liked tracks", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean[]> call, Throwable t) {
                Log.e("Liked tracks", "API call failed: " + t.getMessage());
            }
        });
    }
    public void addItemToPlayList(String accessToken, String playlistId, AddTrackRequest addTrackRequest){
        Call<Void> call = service.addTrackToPlaylist("Bearer " + accessToken, playlistId, addTrackRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    Log.d("AddTrack", "Successfully added track to playlist");
                } else {
                    // Xử lý lỗi
                    Log.e("AddTrack", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi
                Log.e("AddTrack", "Failed to add track: " + t.getMessage());
            }
        });
    }
    public void removeItemToPlayList(String accessToken, String playlistId, RemoveTracksRequest removeTracksRequest){
        Call<Void> call = service.removeTracksFromPlaylist("Bearer " + accessToken, playlistId, removeTracksRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SpotifyAPI", "Tracks removed successfully");
                } else {
                    Log.e("SpotifyAPI", "Failed to remove tracks: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SpotifyAPI", "Error: " + t.getMessage());
            }
        });
    }
    public void removePlaylist(String accessToken, String playlistId){
        Call<Void> call = service.removePlayList("Bearer " + accessToken, playlistId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SpotifyAPI", "Playlist removed successfully");
                } else {
                    Log.e("SpotifyAPI", "Failed to remove Playlist: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SpotifyAPI", "Error: " + t.getMessage());
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
    public interface OnUserCurrentPlaylistLoadedListener {
        void onUserCurrentPlaylistLoaded(List<UserPlayList> userCurrentPlayList);

        void onFailure(String errorMessage);
    }

    public interface OnCreatePlayListLoadedListener {
        void OnCreatePlaylistLoaded(List<PlaylistBase> playlist);

        void onFailure(String errorMessage);
    }
    public interface OnTrackSavedStatusListener {
        void onTrackSavedStatusReceived(boolean isSaved);
    }


}
