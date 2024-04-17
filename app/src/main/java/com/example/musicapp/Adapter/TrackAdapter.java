package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private List<PlaylistTrack> trackList;
    private Context context;

    public TrackAdapter(Context context, List<PlaylistTrack> trackList) {
        this.context = context;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistTrack track = trackList.get(position);
        holder.bind(track);
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView trackImageView;
        private TextView trackNameTextView;
        private TextView artistNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trackImageView = itemView.findViewById(R.id.trackImage);
            trackNameTextView = itemView.findViewById(R.id.trackNameTextView);
            artistNameTextView = itemView.findViewById(R.id.artistsNameTextView);
        }

        public void bind(PlaylistTrack playlistTrack) {
            // Set data for views
            trackNameTextView.setText(playlistTrack.track.name);
            artistNameTextView.setText(playlistTrack.track.artists.get(0).name);
            // Load track image if available
            if (playlistTrack.track.album.images.get(0).url != null && !playlistTrack.track.album.images.get(0).url.isEmpty()) {
                Picasso.get().load(playlistTrack.track.album.images.get(0).url).into(trackImageView);
            } else {
                // If image URL is not available, load a default image
                Picasso.get().load(R.drawable.ariana).into(trackImageView);
            }
        }
    }
}
