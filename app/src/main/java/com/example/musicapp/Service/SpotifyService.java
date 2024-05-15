package com.example.musicapp.Service;

import android.app.appsearch.SearchResult;

import com.example.musicapp.Model.AddTrackRequest;
import com.example.musicapp.Model.EditPlayListRequest;
import com.example.musicapp.Model.FeaturedPlaylists;
import com.example.musicapp.Model.PlaylistBase;
import com.example.musicapp.Model.PlaylistBody;
import com.example.musicapp.Model.PlaylistSimple;
import com.example.musicapp.Model.RemoveTracksRequest;
import com.example.musicapp.Model.TokenResponse;
import com.example.musicapp.Model.UserPlayList;

import org.checkerframework.checker.fenum.qual.PolyFenum;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistFollowPrivacy;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.PlaylistsPager;
import kaaes.spotify.webapi.android.models.TracksPager;
import kaaes.spotify.webapi.android.models.UserPrivate;
import kaaes.spotify.webapi.android.models.UserPublic;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<FeaturedPlaylists> getFeaturedPlaylists(
            @Header("Authorization") String accessToken,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("locale") String locale);
    @GET("playlists/{playlist_id}")
    Call<Playlist> getPlayList(@Path("playlist_id") String playlist_id, @Header("Authorization") String accessToken);
    @GET("search")
    Call<TracksPager> searchTracks(
            @Header("Authorization") String accessToken,
            @Query("q") String query,
            @Query("type") List<String> type,
            @Query("limit") int limit,
            @Query("offset") int offset, @Query("market") String market
    );
    @GET("me/playlists")
    Call<UserPlayList> getCurrentUserPlaylists(
            @Header("Authorization") String accessToken,
            @Query("limit") int limit,
            @Query("offset") int offset);
    @GET("users/{user_id}/playlists")
    Call<PlaylistsPager> getUserPlaylists(
            @Header("Authorization") String accessToken,
            @Path("user_id") String user_id,
            @Query("limit") int limit,
            @Query("offset") int offset);
    @POST("users/{user_id}/playlists")
    Call<PlaylistBase> createPlaylists(
            @Header("Authorization") String accessToken,
            @Path("user_id") String user_id,
            @Body PlaylistBody playlistBody);
    @GET("me/tracks/contains")
    Call<Boolean[]> checkUsersSavedTracks(
            @Header("Authorization") String accessToken,
            @Query("ids") String trackIds
    );

    @POST("playlists/{playlist_id}/tracks")
    Call<Void> addTrackToPlaylist(
            @Header("Authorization") String accessToken,
            @Path("playlist_id") String playlistId,
            @Body AddTrackRequest addTrackRequest
    );
    @HTTP(method = "DELETE", path = "playlists/{playlist_id}/tracks", hasBody = true)
    Call<Void> removeTracksFromPlaylist(
            @Header("Authorization") String accessToken,
            @Path("playlist_id") String playlistId,
            @Body RemoveTracksRequest body
    );
    @DELETE("playlists/{playlist_id}/followers")
    Call<Void> removePlayList(@Header("Authorization") String accessToken,@Path("playlist_id") String playlistId);
    @PUT("playlists/{playlist_id}/images")
    Call<ResponseBody> uploadPlaylistImage(
            @Path("playlist_id") String playlistId,
            @Header("Authorization") String authorization,
            @Body String base64Image
    );
    @PUT("playlists/{playlist_id}")
    Call<Void> updatePlaylist(
            @Header("Authorization") String accessToken,
            @Path("playlist_id") String playlistId,
            @Body EditPlayListRequest request
    );
}

