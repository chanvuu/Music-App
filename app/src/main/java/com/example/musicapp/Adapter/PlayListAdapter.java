package com.example.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.DisplayTrackActivity;
import com.example.musicapp.Model.PlaylistSimple;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    private List<PlaylistSimple> playlists;
    private Context context;

    public PlayListAdapter(Context context, List<PlaylistSimple> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistSimple playlist = playlists.get(position);
        holder.bind(playlist);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView playListImageView;
        private TextView playListNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playListImageView = itemView.findViewById(R.id.playListImageView);
            playListNameTextView = itemView.findViewById(R.id.playListNameTextView);
            playListImageView.setOnClickListener(this);
            playListNameTextView.setOnClickListener(this);
        }

        public void bind(PlaylistSimple playlists) {
            // Gán dữ liệu cho các thành phần giao diện
            playListNameTextView.setText(playlists.name);
            // Kiểm tra nếu danh sách hình ảnh không rỗng
            if (!playlists.images.get(0).url.isEmpty()) {
                String imageUrl = playlists.images.get(0).url;
                Picasso.get().load(imageUrl).into(playListImageView);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAbsoluteAdapterPosition();
            PlaylistSimple playlist = playlists.get(position);
            Log.d("abc", playlist.name);
            Intent intent = new Intent(context, DisplayTrackActivity.class);
            intent.putExtra("playlistId", playlist.id);
            intent.putExtra("playlistImageUrl", playlist.images.get(0).url);
            intent.putExtra("playlistName", playlist.name);
            intent.putExtra("tracks", playlist.tracks);
            intent.putExtra("description", playlist.description);
            Log.d("inform track", playlist.tracks.href);

//            // Bắt đầu Activity mới
            context.startActivity(intent);
        }
    }
}
