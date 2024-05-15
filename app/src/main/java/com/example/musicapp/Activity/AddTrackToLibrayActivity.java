package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicapp.Adapter.AddTrackToPlayListAdapter;
import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Model.AddTrackRequest;
import com.example.musicapp.Model.RemoveTracksRequest;
import com.example.musicapp.Model.UserPlayList;
import com.example.musicapp.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddTrackToLibrayActivity extends AppCompatActivity {
    private final PlayListData playListData = new PlayListData();
    private ImageView profileImageView;
    private ImageButton backButton;
    private MaterialButton addButton;
    private List<UserPlayList.PlaylistItem> playLists;
    private AddTrackToPlayListAdapter libraryAdapter;
    private RecyclerView recyclerView;
    private Intent intent;

    private String accessToken = "";
    private String trackId;
    private String trackUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track_to_libray);
        intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        trackId = intent.getStringExtra("trackId");
        trackUri = intent.getStringExtra("trackUri");

        recyclerView = findViewById(R.id.libraryRecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        playLists = new ArrayList<>();
        libraryAdapter = new AddTrackToPlayListAdapter(this, playLists);
        recyclerView.setAdapter(libraryAdapter);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> handleAddButtonClick());

        libraryAdapter.setTrackId(trackId);
        libraryAdapter.setAccessToken(accessToken);
        loadPlayList(accessToken);
    }

    private void handleAddButtonClick() {
        List<String> selectedPlaylistIds = libraryAdapter.getSelectedPlaylistIds();
        List<String> unSelectedPlaylistIds = libraryAdapter.getUnSelectedPlaylistIds();
        Map<String, Boolean> playlistInfoMap = libraryAdapter.getPlaylistInfoMap();

        Log.d("playlistInfoMap", playlistInfoMap.toString());

        List<RemoveTracksRequest.TrackUri> trackUris = new ArrayList<>();
        trackUris.add(new RemoveTracksRequest.TrackUri(trackUri));
        RemoveTracksRequest removeTracksRequest = new RemoveTracksRequest();
        removeTracksRequest.tracks = trackUris;

        for (Map.Entry<String, Boolean> entry : playlistInfoMap.entrySet()) {
            String playlistId = entry.getKey();
            boolean isChecked = entry.getValue();

            if (isChecked) {
                playListData.removeItemToPlayList(accessToken, playlistId, removeTracksRequest);
            } else {
                playListData.addItemToPlayList(accessToken, playlistId, new AddTrackRequest(new String[]{trackUri}, 0));
            }
        }

        libraryAdapter.notifyDataSetChanged(); // Update adapter after operations
        Toast.makeText(this,  "Track updated in playlists", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void loadPlayList(String accessToken) {
        playListData.getUserCurrentPlayList(accessToken, 20, 0, new PlayListData.OnUserCurrentPlaylistLoadedListener() {
            @Override
            public void onUserCurrentPlaylistLoaded(List<UserPlayList> userCurrentPlayList) {
                if (userCurrentPlayList != null) {
                    for (UserPlayList userPlayList : userCurrentPlayList) {
                        if (userPlayList != null && !userPlayList.items.isEmpty()) {
                            playLists.addAll(userPlayList.items);
                        }
                    }
                    libraryAdapter.updatePlaylists(playLists);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("fail", errorMessage);
            }
        });
    }
}
