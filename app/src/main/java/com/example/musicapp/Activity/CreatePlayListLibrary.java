package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Model.PlaylistBody;
import com.example.musicapp.R;
import com.google.android.material.button.MaterialButton;

public class CreatePlayListLibrary extends AppCompatActivity {
    private MaterialButton createButton, cancelButton;
    private EditText playlistNameEditText;
    private final PlayListData playListData = new PlayListData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_play_list_form);
        createButton = findViewById(R.id.createButton);
        cancelButton = findViewById(R.id.cancelButton);
        playlistNameEditText = findViewById(R.id.playlistName);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistBody playlistBody = new PlaylistBody();
                String playlistName = String.valueOf(playlistNameEditText.getText());
                playlistBody.setName(playlistName);
                playlistBody.setDescription("");
                playlistBody.setPublicc(true);
                Intent intent = getIntent();
                String accessToken = intent.getStringExtra("accessToken");
                String userId = intent.getStringExtra("userId");
                Log.d("userId", userId);
                playListData.createPlayList(accessToken, userId, playlistBody);
                Toast.makeText(CreatePlayListLibrary.this, "Playlist created successfully", Toast.LENGTH_SHORT).show();
                // Trả kết quả về cho LibraryFragment
                Intent resultIntent = new Intent();
                resultIntent.putExtra("playlistCreated", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
