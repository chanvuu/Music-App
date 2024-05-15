package com.example.musicapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Empty;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.ImageUri;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import com.squareup.picasso.Picasso;

public class PlayerActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "dac6319dfb5c4831978c47ba5c9fdaff";
    private static final String REDIRECT_URI = "http://com.localhost.musicapp/callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private TextView playlistNameTextView, trackNameTextView, artistNameTextView, total_time, current_time;
    private ImageView trackImgView;
    private ImageButton  playpauseButton, nextButton, previousButton;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    final int[] progress = {0}; // Giá trị ban đầu current time seekbar
    private String trackUri = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        playlistNameTextView = findViewById(R.id.playlist_name);
        trackImgView = findViewById(R.id.track_image);
        trackNameTextView = findViewById(R.id.track_name);
        artistNameTextView = findViewById(R.id.artist_name);
        playpauseButton = findViewById(R.id.play_pause);
        nextButton = findViewById(R.id.next);
        previousButton = findViewById(R.id.previous);
        total_time = findViewById(R.id.total_time);
        current_time = findViewById(R.id.current_time);
        seekBar = findViewById(R.id.seek_bar);
        loadData();
        onStart();
        startAutoUpdateSeekBar();

    }

    private void loadData(){
        Intent intent = getIntent();
        String trackId = intent.getStringExtra("trackId");
        trackUri = intent.getStringExtra("trackUri");
        Log.d("trackId", trackUri);
        String trackName = intent.getStringExtra("trackName");
        String playlistName = intent.getStringExtra("playlistName");
        String artistName = intent.getStringExtra("artistName");
        String trackImg = intent.getStringExtra("trackImg");
        String trackDuration = intent.getStringExtra("trackDuration");
        if (trackName != null) {
            trackNameTextView.setText(trackName);
        } else {
            Log.e("Error", "trackName is null");
        }

        if (playlistName != null) {
            playlistNameTextView.setText(playlistName);
        } else {
            Log.e("Error", "playlistName is null");
        }

        if (artistName != null) {
            artistNameTextView.setText(artistName);
        } else {
            Log.e("Error", "artistName is null");
        }

        if (trackDuration != null) {
            total_time.setText(convertSecondsToTime(Long.parseLong(trackDuration)/1000));
            seekBar.setMax(Integer.parseInt(trackDuration)/1000);
            seekBar.setProgress(0);
        } else {
            Log.e("Error", "trackDuration is null");
        }

        if (!trackImg.isEmpty()) {
            //set ảnh cho profile avatar
            Picasso.get().load(trackImg).into(trackImgView);
        }
    }
    private void loadData(String trackUri, String trackName, String playlistName, String artistName, String trackImg, String trackDuration){
        this.trackUri = trackUri;
        if (trackName != null) {
            trackNameTextView.setText(trackName);
        } else {
            Log.e("Error", "trackName is null");
        }

        if (playlistName != null) {
            playlistNameTextView.setText(playlistName);
        } else {
            Log.e("Error", "playlistName is null");
        }

        if (artistName != null) {
            artistNameTextView.setText(artistName);
        } else {
            Log.e("Error", "artistName is null");
        }

        if (trackDuration != null) {
            total_time.setText(convertSecondsToTime(Long.parseLong(trackDuration)/1000));
            seekBar.setMax(Integer.parseInt(trackDuration)/1000);
            seekBar.setProgress(0);
        } else {
            Log.e("Error", "trackDuration is null");
        }

        if (!trackImg.isEmpty()) {
            trackImg = "https://i.scdn.co/image/" + trackImg.split(":")[2]; // Lấy phần cuối của URI
            Log.d("haha", trackImg);
            //set ảnh cho profile avatar
            Picasso.get().load(trackImg).into(trackImgView);
        }
    }

    public String convertSecondsToTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        // Định dạng chuỗi thời gian
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds); // Định dạng "hh:mm:ss"
        } else {
            return String.format("%d:%02d", minutes, seconds); // Định dạng "mm:ss"
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        Intent intent = getIntent();
                        String trackId = intent.getStringExtra("trackId");
                        String trackUri = intent.getStringExtra("trackUri");
                        onPlay(trackUri);
                        buttonControl(trackUri);
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }
    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void onPlay(String trackUri) {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play(trackUri);
        mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
            if (playerState.track != null) {
                String trackUri1 = playerState.track.uri; // Đây là URI của bản nhạc đang phát
                System.out.println("Track URI: " + trackUri1);
                if (!playerState.isPaused && playerState.playbackPosition >= playerState.track.duration) {
                    // Chuyển đến bài hát tiếp theo khi bài hát kết thúc
                    mSpotifyAppRemote.getPlayerApi().pause();
                }
            } else {
                System.out.println("Không có bản nhạc đang phát");
            }

        });
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }
    private void startAutoUpdateSeekBar() {
        Runnable updateRunnable = new Runnable() { // Tạo Runnable
            @Override
            public void run() {
                mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
                    if (playerState.track != null && !playerState.isPaused) { // Kiểm tra trạng thái
                        long totalTime = playerState.track.duration;

                        if (progress[0] < totalTime / 1000) { // Kiểm tra điều kiện
                            progress[0]++; // Tăng giá trị hiện tại
                            seekBar.setProgress(progress[0]); // Cập nhật SeekBar
                            current_time.setText(convertSecondsToTime(progress[0])); // Cập nhật thời gian

                            // Lặp lại sau 1 giây
                            handler.postDelayed(this, 1000);
                        } else {
                            stopAutoUpdateSeekBar(); // Dừng nếu đạt đến tổng thời gian
                        }
                    }
                });
            }
        };

        // Bắt đầu quá trình tự động cập nhật
        handler.postDelayed(updateRunnable, 1000);
    }

    private void stopAutoUpdateSeekBar() {
        handler.removeCallbacksAndMessages(null);
    }
    private void buttonControl(String trackUri){
        playpauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hành động khi nhấn vào nút phát/tạm dừng
                mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(new CallResult.ResultCallback<PlayerState>() {
                    @Override
                    public void onResult(PlayerState playerState) {
                        // Kiểm tra xem nhạc có đang phát không
                        if (playerState.isPaused) {
                            // Nếu nhạc đang tạm dừng, phát nhạc
                            mSpotifyAppRemote.getPlayerApi().resume();
                            startAutoUpdateSeekBar();
                            playpauseButton.setImageResource(R.drawable.baseline_pause_white_36);

                        } else {
                            // Nếu nhạc đang phát, tạm dừng
                            mSpotifyAppRemote.getPlayerApi().pause();
                            startAutoUpdateSeekBar();
                            playpauseButton.setImageResource(R.drawable.baseline_play_white_36);
                        }
                    }
                });
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang bài hát tiếp theo
                mSpotifyAppRemote.getPlayerApi().skipNext().setResultCallback(result -> {
                    Log.d("NextSong", "Bài hát tiếp theo được chọn");

                    // Cập nhật dữ liệu sau khi chuyển sang bài hát tiếp theo
                    updateData();
                    stopAutoUpdateSeekBar();
                    startAutoUpdateSeekBar();
                    playpauseButton.setImageResource(R.drawable.baseline_pause_white_36);
                }).setErrorCallback(error -> {
                    Log.e("SkipNextError", "Lỗi khi chuyển sang bài hát tiếp theo: " + error.getMessage());
                });
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang bài hát tiếp theo
                mSpotifyAppRemote.getPlayerApi().skipPrevious().setResultCallback(result -> {
                    Log.d("NextSong", "Bài hát tiếp theo được chọn");

                    // Cập nhật dữ liệu sau khi chuyển sang bài hát tiếp theo
                    updateData();
                    stopAutoUpdateSeekBar();
                    startAutoUpdateSeekBar();
                }).setErrorCallback(error -> {
                    Log.e("SkipNextError", "Lỗi khi chuyển sang bài hát tiếp theo: " + error.getMessage());
                });
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressSeekbar, boolean fromUser) {
                if (fromUser) {
                    // Nếu người dùng thay đổi SeekBar, điều chỉnh nội dung tương ứng
                    progress[0] = progressSeekbar;
                    long positionMs = progressSeekbar * 1000; // Chuyển đổi thành mili giây

                    mSpotifyAppRemote.getPlayerApi().seekToRelativePosition(30000).setResultCallback(result -> {
                        Log.d("SeekTo", "Seek thành công đến " + 30000); // Gọi thành công
                    }).setErrorCallback(error -> {
                        Log.e("SeekToError", "Lỗi khi gọi seekTo: " + error.getMessage()); // Gọi thất bại
                    });

//
//                    mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
//                        long totalTime = playerState.track.duration; // Tổng thời gian của bài hát
//                        if (positionMs < totalTime) {
//                            mSpotifyAppRemote.getPlayerApi().seekTo(positionMs); // Gọi seekTo
//                        } else {
//                            Log.e("SeekToError", "Giá trị vượt quá tổng thời gian của bài hát");
//                        }
//                    });

                    current_time.setText(convertSecondsToTime(progressSeekbar)); // Cập nhật thời gian
                    // Thực hiện hành động tương ứng, ví dụ: tua bài hát đến vị trí mới
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Thực hiện hành động khi người dùng bắt đầu kéo SeekBar

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Thực hiện hành động khi người dùng dừng kéo SeekBar
            }
        });
    }

    private void updateData(){
        mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(new CallResult.ResultCallback<PlayerState>() {
            @Override
            public void onResult(PlayerState playerState) {
                progress[0] = 0;
                String trackUri = playerState.track.uri;
                String trackName = playerState.track.name;
                String playlistName = playerState.track.album.name;
                String artistName = playerState.track.artist.name;
                String trackImg = playerState.track.imageUri.raw;
                String trackDuration = String.valueOf(playerState.track.duration);
                loadData(trackUri, trackName, playlistName, artistName, trackImg, trackDuration);
            }
        });
    }
}
