package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.PopupMenu;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.musicapp.Adapter.AlbumAdapter;
import com.example.musicapp.Adapter.PlayListAdapter;



import com.example.musicapp.Adapter.TrackAdapter;
import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Model.PlaylistBase;
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

    private ImageButton moreButton;




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

            Log.d("playlistId", playlistId);
            Log.d("accessToken", accessToken);


            // Hiển thị dữ liệu
            ImageView imageView = findViewById(R.id.playListImageView);
            TextView playListNametextView = findViewById(R.id.playListNameTextView);
            TextView descriptionTextView = findViewById(R.id.playListDesTextView);
            moreButton = findViewById(R.id.moreButton);
            playListNametextView.setText(playlistName);
            descriptionTextView.setText(description);

            Picasso.get().load(playlistImageUrl).into(imageView);

            if (!playlistImageUrl.isEmpty() && playlistImageUrl != null){
                Picasso.get().load(playlistImageUrl).into(imageView);
            }else{
                Picasso.get().load(R.drawable.liked).into(imageView);
            }

            // Hien thi du lieu track
            recyclerView = findViewById(R.id.trackRecycleview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager
            tracks = new ArrayList<>();
            trackAdapter = new TrackAdapter(DisplayTrackActivity.this, tracks);
            recyclerView.setAdapter(trackAdapter);

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(DisplayTrackActivity.this, v);
                    popupMenu.getMenuInflater().inflate(R.menu.playlist_options_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_edit_playlist:
                                    // Handle edit playlist
                                    Intent intent = new Intent(DisplayTrackActivity.this, EditPlayListActivity.class);

//                                    Log.d("userId", userId);
                                    intent.putExtra("accessToken", accessToken);
                                    intent.putExtra("playlistId", playlistId);
                                    intent.putExtra("playlistName", playlistName);
                                    intent.putExtra("description", description);

                                    // Khởi chạy activity mới để tạo playlist
                                    startActivity(intent);
                                    // Your edit playlist logic here
                                    return true;
                                case R.id.action_delete_playlist:
                                    // Handle delete playlist
                                    PlayListData playListData = new PlayListData();
                                    playListData.removePlaylist(accessToken, playlistId);
                                    Toast.makeText(DisplayTrackActivity.this, "Delete Playlist Clicked", Toast.LENGTH_SHORT).show();
                                    finish();
                                    // Your delete playlist logic here
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popupMenu.show();
                }
            });

            loadTracks(accessToken ,playlistId);
        }
    }

    private void loadTracks(String accessToken, String playlistId) {
        Log.d("accessToken sau khi đăng nhập1: ", accessToken);

        PlayListData playListData = new PlayListData();

        Log.d("acsadasd", accessToken);
        Log.d("acsadasd", playlistId);

        playListData.getPlaylist(playlistId, accessToken, new PlayListData.OnPlaylistLoadedListener1() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onPlaylistLoaded(Playlist playlist) {

                Log.d("test get track name",playlist.tracks.items.get(0).track.name);
                Log.d("test get track name",playlist.tracks.items.get(1).track.name);
                tracks.addAll(playlist.tracks.items);
                trackAdapter.notifyDataSetChanged();


                if (playlist.tracks != null && !playlist.tracks.items.isEmpty()){
                    Log.d("test get track name",playlist.tracks.items.get(0).track.name);
                    trackAdapter.setAccessToken(accessToken);
                    trackAdapter.setPlaylistId(playlistId);
                    trackAdapter.updatePlaylists(playlist.tracks.items);
                }

            }

            @Override
            public void onFailure(String errorMessage) {



                Log.e("error", errorMessage);

            }
        });
    }
}