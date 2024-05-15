package com.example.musicapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Model.UserPlayList;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;

public class AddTrackToPlayListAdapter extends RecyclerView.Adapter<AddTrackToPlayListAdapter.ViewHolder> {
    private List<UserPlayList.PlaylistItem> playlists;
    private Context context;
    private String accessToken, trackId;
    private List<Boolean> checkStatusList;
    private List<String> selectedPlaylistIds = new ArrayList<>();
    private List<String> unSelectedPlaylistIds = new ArrayList<>();
    private Map<String, Boolean> playlistInfoMap = new HashMap<>();

    public AddTrackToPlayListAdapter(Context context, List<UserPlayList.PlaylistItem> playlists) {
        this.context = context;
        this.playlists = playlists;
        checkStatusList = new ArrayList<>(Collections.nCopies(playlists.size(), false));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_add_playlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserPlayList.PlaylistItem playlist = playlists.get(position);
        holder.bind(playlist);
        boolean isChecked = playlistInfoMap.getOrDefault(playlist.id, false);
        holder.updateCheckImage(isChecked);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public void updatePlaylists(List<UserPlayList.PlaylistItem> playlists) {
        this.playlists = playlists;
        checkStatusList = new ArrayList<>(Collections.nCopies(playlists.size(), false));
        notifyDataSetChanged();
    }

    public List<String> getSelectedPlaylistIds() {
        return selectedPlaylistIds;
    }

    public void setSelectedPlaylistIds(List<String> selectedPlaylistIds) {
        this.selectedPlaylistIds = selectedPlaylistIds;
    }

    public List<String> getUnSelectedPlaylistIds() {
        return unSelectedPlaylistIds;
    }

    public void setUnSelectedPlaylistIds(List<String> unSelectedPlaylistIds) {
        this.unSelectedPlaylistIds = unSelectedPlaylistIds;
    }

    public Map<String, Boolean> getPlaylistInfoMap() {
        return playlistInfoMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView playListImageView;
        private TextView playListNameTextView, totalTrackTextView;
        private ImageButton checkButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playListImageView = itemView.findViewById(R.id.UserPlayListImage);
            playListNameTextView = itemView.findViewById(R.id.UserPlayListNameTextView);
            totalTrackTextView = itemView.findViewById(R.id.totalTrack);
            checkButton = itemView.findViewById(R.id.checkButton);
            playListImageView.setOnClickListener(this);
            playListNameTextView.setOnClickListener(this);
            totalTrackTextView.setOnClickListener(this);
            checkButton.setOnClickListener(this);
        }

        public void bind(UserPlayList.PlaylistItem playlist) {
            playListNameTextView.setText(playlist.name);
            totalTrackTextView.setText(playlist.tracks.total + " bài hát");
            if (playlist.images != null && !playlist.images.isEmpty() && playlist.images.get(0).url != null && !playlist.images.get(0).url.isEmpty()) {
                String imageUrl = playlist.images.get(0).url;
                Picasso.get().load(imageUrl).into(playListImageView);
            } else {
                Picasso.get().load(R.drawable.liked).into(playListImageView);
            }
            checkTrackSavedStatus(playlist.id);
        }

        @Override
        public void onClick(View v) {
            int position = getAbsoluteAdapterPosition();
            boolean currentSavedStatus = checkStatusList.get(position);
            checkStatusList.set(position, !currentSavedStatus);
            updateCheckImage(!currentSavedStatus);

            String playlistId = playlists.get(position).id;

            if (!currentSavedStatus) {
                selectedPlaylistIds.add(playlistId);
                unSelectedPlaylistIds.remove(playlistId);
                playlistInfoMap.put(playlistId, true);
            } else {
                selectedPlaylistIds.remove(playlistId);
                unSelectedPlaylistIds.add(playlistId);
                playlistInfoMap.put(playlistId, false);
            }
        }

        public void updateCheckImage(boolean isChecked) {
            if (isChecked) {
                checkButton.setImageResource(R.drawable.circle_check);
            } else {
                checkButton.setImageResource(R.drawable.circle_outline);
            }
        }

        private void checkTrackSavedStatus(String playlistId) {
            PlayListData playListData = new PlayListData();
            playListData.getPlaylist(playlistId, accessToken, new PlayListData.OnPlaylistLoadedListener1() {
                @Override
                public void onPlaylistLoaded(Playlist playlist) {
                    if (playlist != null) {
                        boolean trackFound = false;
                        for (PlaylistTrack tracks : playlist.tracks.items) {
                            if (Objects.equals(trackId, tracks.track.id)) {
                                trackFound = true;
                                break;
                            }
                        }
                        if (trackFound) {
                            updateCheckImage(true);
                            checkStatusList.set(getAbsoluteAdapterPosition(), true);
                            playlistInfoMap.put(playlistId, true);
                        } else {
                            playlistInfoMap.put(playlistId, false);
                        }
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    playlistInfoMap.put(playlistId, false);
                }
            });
        }
    }
}
