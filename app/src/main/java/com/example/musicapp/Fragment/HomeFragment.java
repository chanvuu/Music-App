package com.example.musicapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.EditProfileActivity;
import com.example.musicapp.Adapter.AlbumAdapter;
import com.example.musicapp.Adapter.PlayListAdapter;
import com.example.musicapp.Data.AlbumData;
import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Model.PlaylistSimple;
import com.example.musicapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.FeaturedPlaylists;
import kaaes.spotify.webapi.android.models.Playlist;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private ImageView profileImageView;
    private AlbumAdapter adapter;
    private List<AlbumSimple> albumList;

    private PlayListAdapter playListAdapter;
    private List<PlaylistSimple> playlists;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("hello", "hello");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home, container, false);
        recyclerView = view.findViewById(R.id.albumRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        albumList = new ArrayList<>();
//        adapter = new AlbumAdapter(getActivity(), albumList);
        playlists = new ArrayList<>();
        playListAdapter = new PlayListAdapter(getActivity(), playlists);
        recyclerView.setAdapter(playListAdapter);
        //set onclick cho avatar
        profileImageView = view.findViewById(R.id.profile_avatar);
        profileImageView.setOnClickListener(this);
        loadData();

        return view;
    }

    private void loadPlayList(String accessToken) {
        Log.d("accessToken sau khi đăng nhập1: ", accessToken);

        PlayListData playListData = new PlayListData();
        playListData.getPlaylist("37i9dQZF1DWVOaOWiVD1Lf", accessToken, new PlayListData.OnPlaylistLoadedListener1() {
            @Override
            public void onPlaylistLoaded(Playlist playlist) {
                Log.d("test get track name",playlist.tracks.items.get(0).track.name);

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
        playListData.getFeaturedPlaylists(accessToken, 20, 0, "vi_VN", new PlayListData.OnPlayListLoadedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onPlayListLoaded(List<PlaylistSimple> featuredPlaylists) {
                playlists.addAll(featuredPlaylists);
                Log.d("abc", playlists.get(0).name);
                playListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Spotify API", "Fail to get featured playlist");
            }
        });
    }

    private void loadData() {
        //get user id after login
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String userId = intent.getStringExtra("userId");
//            Log.d("UserId", userId);
            if (userId != null) {
                //get accesstoken of user id in fire base
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String accessToken = dataSnapshot.child("accessToken").getValue(String.class);
                            String imgURL = dataSnapshot.child("imgURL").getValue(String.class);
                            if (accessToken != null && !accessToken.isEmpty()) {
                                // Gọi API của Spotify để lấy dữ liệu sử dụng accessToken
                                loadPlayList(accessToken);
                                //Load img cho user
                                assert imgURL != null;
                                if (!imgURL.isEmpty()) {
                                    //set ảnh cho profile avatar
                                    Picasso.get().load(imgURL).into(profileImageView);
                                }
//                                Log.d("accessToken sau khi đăng nhập: ", accessToken);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Lỗi: " + databaseError.getMessage());
                    }
                });
            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }
}
