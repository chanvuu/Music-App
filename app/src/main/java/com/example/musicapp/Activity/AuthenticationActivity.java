package com.example.musicapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.Data.AlbumData;
import com.example.musicapp.R;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import kaaes.spotify.webapi.android.models.Album;


public class AuthenticationActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "32f239026d4c4102b4a5be8f6e71f9cb";
    private static final String REDIRECT_URI = "music-app-login://callback";
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Tạo AuthorizationRequest
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI
        );
        builder.setScopes(new String[]{"streaming"});
        AuthorizationRequest request = builder.build();

        // Bắt đầu quá trình đăng nhập
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    // Xử lý kết quả sau khi đăng nhập
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    String accessToken = response.getAccessToken();
                    AlbumData spotifyApiClient = new AlbumData();
                    String albumId = "4aawyAB9vmqN3uQ7FjRGTy"; // Thay thế bằng id thực của album bạn muốn lấy

                    spotifyApiClient.getAlbum(albumId, accessToken, new AlbumData.OnAlbumLoadedListener() {
                        @Override
                        public void onAlbumLoaded(Album album) {
                            Log.d("AlbumName", "Album: " + album.name);

                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Xử lý lỗi nếu cần
                            Log.e("Error","false to get album");

                        }
                    });

                    Log.d("accessToken",accessToken);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.e("Error","false to get token");

                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
}
