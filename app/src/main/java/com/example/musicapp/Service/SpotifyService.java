package com.example.musicapp.Service;

import com.example.musicapp.Model.FeaturedPlaylists;
import com.example.musicapp.Model.PlaylistBase;
import com.example.musicapp.Model.PlaylistSimple;
import com.example.musicapp.Model.TokenResponse;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistsPager;
import kaaes.spotify.webapi.android.models.UserPrivate;
import kaaes.spotify.webapi.android.models.UserPublic;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @GET("albums")
    Call<Album> getAlbum(@Header("Authorization") String accessToken);
    @GET("me")
    Call<UserPublic> getCurrentUser(@Header("Authorization") String accessToken);
    @GET("browse/new-releases")
    Call<AlbumsPager> getNewReleases(@Header("Authorization") String accessToken, @Query("limit") int limit);
    @GET("browse/featured-playlists")
    Call<FeaturedPlaylists> getFeaturedPlaylists(@Header("Authorization") String accessToken, @Query("limit") int limit, @Query("offset") int offset, @Query("locale") String locale);
    @GET("playlists/{playlist_id}")
    Call<Playlist> getPlayList(@Path("playlist_id") String playlist_id, @Header("Authorization") String accessToken);
//    @GET("search")
//    Call<PlaylistSimple> search(@Header("Authorization") String accessToken, @Query("q") String q, @Query("type") String type);

}

