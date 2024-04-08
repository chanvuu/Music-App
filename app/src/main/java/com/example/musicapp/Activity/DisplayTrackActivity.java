package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Model.PlaylistBase;
import com.example.musicapp.Model.PlaylistSimple;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DisplayTrackActivity extends AppCompatActivity {
    private List<PlaylistBase> playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_track);
        // Nhận dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String playlistId = extras.getString("playlistId");
            String playlistImageUrl = extras.getString("playlistImageUrl");
            String playlistName = extras.getString("playlistName");
            String description = extras.getString("description");

            // Hiển thị dữ liệu
            ImageView imageView = findViewById(R.id.playListImageView);
            TextView playListNametextView = findViewById(R.id.playListNameTextView);
            TextView descriptionTextView = findViewById(R.id.playListDesTextView);
            playListNametextView.setText(playlistName);
            descriptionTextView.setText(description);
            Picasso.get().load(playlistImageUrl).into(imageView);
        }
    }

    public void loadAlbum(String playlistId, String accessToken){
        Log.d("accessToken sau khi đăng nhập2: ", accessToken);
//
//        PlayListData playListData = new PlayListData();
//        playListData.getPlaylist(playlistId, accessToken, new PlayListData.OnPlayListBaseLoadedListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onPlayListLoaded(PlaylistBase playlistBase) {
//                playlists.addAll(playlistBase);
//                Log.d("abc", playlists.get(0).name);
////                playListAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                Log.e("Spotify API", "Fail to get featured playlist");
//            }
//        });
    }
}