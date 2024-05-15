package com.example.musicapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.Data.AlbumData;
import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Data.UserData;
import com.example.musicapp.MainActivity;
import com.example.musicapp.Model.User;
import com.example.musicapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.FeaturedPlaylists;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.UserPublic;


public class AuthenticationActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "dac6319dfb5c4831978c47ba5c9fdaff";
    private static final String REDIRECT_URI = "music-app-login://callback";
    private static final int REQUEST_CODE = 1337;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        FirebaseApp.initializeApp(this);
        // Tạo AuthorizationRequest
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI
        ).setShowDialog(true);
        builder.setScopes(new String[]{"user-read-email", "user-read-private", "streaming" ,
                "playlist-read-private", "playlist-modify-public" , "ugc-image-upload",
                "playlist-modify-private", "playlist-read-collaborative", "user-library-read"});
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
                    waitForUserInfo(accessToken);


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

    void waitForUserInfo(String accessToken) {
        UserData userData = new UserData();
        userData.getUser(accessToken, new UserData.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(UserPublic userPublic) {
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                String imgURL = userPublic.images.isEmpty() ? "" : userPublic.images.get(0).url;
                User user = new User(userPublic.id, userPublic.display_name, accessToken, imgURL);
                usersRef.child(userPublic.id).setValue(user);

                Log.d("user Name", userPublic.display_name);
                //luu du lieu vao database
                startMainActivity(userPublic.id);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }


    private void startMainActivity(String userId) {
        Log.d("hello", "hello");
        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

}
