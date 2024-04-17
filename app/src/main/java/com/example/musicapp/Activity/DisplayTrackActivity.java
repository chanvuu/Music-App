package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapp.Adapter.AlbumAdapter;
import com.example.musicapp.Adapter.PlayListAdapter;
import com.example.musicapp.Adapter.TrackAdapter;
import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Model.PlaylistBase;
import com.example.musicapp.Model.PlaylistSimple;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class DisplayTrackActivity extends AppCompatActivity {
    private List<PlaylistBase> playlists;
    private List<PlaylistTrack> tracks;
    private TrackAdapter trackAdapter;
    private RecyclerView recyclerView;



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
            String accessToken = extras.getString("accessToken");

            // Hiển thị dữ liệu
            ImageView imageView = findViewById(R.id.playListImageView);
            TextView playListNametextView = findViewById(R.id.playListNameTextView);
            TextView descriptionTextView = findViewById(R.id.playListDesTextView);
            playListNametextView.setText(playlistName);
            descriptionTextView.setText(description);
            Picasso.get().load(playlistImageUrl).into(imageView);
            // Hien thi du lieu track
            recyclerView = findViewById(R.id.trackRecycleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager
            tracks = new ArrayList<>();
            trackAdapter = new TrackAdapter(DisplayTrackActivity.this, tracks);
            recyclerView.setAdapter(trackAdapter);

            loadTracks(accessToken ,playlistId);
        }
    }

    private void loadTracks(String accessToken, String playlistId) {
        Log.d("accessToken sau khi đăng nhập1: ", accessToken);

        PlayListData playListData = new PlayListData();
        playListData.getPlaylist(playlistId, accessToken, new PlayListData.OnPlaylistLoadedListener1() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onPlaylistLoaded(Playlist playlist) {
                Log.d("test get track name",playlist.tracks.items.get(0).track.name);
                Log.d("test get track name",playlist.tracks.items.get(1).track.name);
                tracks.addAll(playlist.tracks.items);
                trackAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}