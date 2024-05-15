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

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<AlbumSimple> albumList;
    private Context context;

    public AlbumAdapter(Context context, List<AlbumSimple> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlbumSimple album = albumList.get(position);
        holder.bind(album);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView albumImageView;
        private TextView artistNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImageView = itemView.findViewById(R.id.albumImageView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
        }

        public void bind(AlbumSimple album) {
            // Gán dữ liệu cho các thành phần giao diện
            artistNameTextView.setText(album.name);
            // Kiểm tra nếu danh sách hình ảnh không rỗng
            if (!album.images.get(0).url.isEmpty()) {
                String imageUrl = album.images.get(0).url;
                Picasso.get().load(imageUrl).into(albumImageView);
            }
        }
    }
}
