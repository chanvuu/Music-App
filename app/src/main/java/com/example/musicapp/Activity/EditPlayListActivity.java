package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Model.EditPlayListRequest;
import com.example.musicapp.R;

public class EditPlayListActivity extends AppCompatActivity {
    private PlayListData playListData = new PlayListData();
    private EditText playlistNameEditText, descriptionEditText;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_play_list);

        playlistNameEditText = findViewById(R.id.playlistNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);

        Intent intent = getIntent();
        String accessToken = intent.getStringExtra("accessToken");
        String playlistId = intent.getStringExtra("playlistId");
        String playlistName = intent.getStringExtra("playlistName");
        String description = intent.getStringExtra("description");
        playlistNameEditText.setText(playlistName);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playlistNameEdited = String.valueOf(playlistNameEditText.getText());
                String descriptionEdited = String.valueOf(descriptionEditText.getText());
                Log.d("playlistNameEdited",playlistNameEdited);
                Log.d("descriptionEdited",descriptionEdited);
                Log.d("accessToken",accessToken);
                Log.d("playlistId",playlistId);

                EditPlayListRequest editPlayListRequest = new EditPlayListRequest(playlistNameEdited, true, descriptionEdited, false);
                Log.d("editPlayListRequest.getDescription();",editPlayListRequest.getDescription());
                playListData.updatePlaylist(accessToken, playlistId, editPlayListRequest);
//                finish();
//                Toast.makeText(EditPlayListActivity.this, "Edit Playlist SuccessFully", Toast.LENGTH_SHORT).show();

            }
        });

    }
}