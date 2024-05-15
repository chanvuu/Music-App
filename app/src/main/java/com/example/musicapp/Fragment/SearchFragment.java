package com.example.musicapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.DisplayTrackActivity;
import com.example.musicapp.Adapter.SearchAdapter;
import com.example.musicapp.Adapter.TrackAdapter;
import com.example.musicapp.Data.PlayListData;
import com.example.musicapp.Data.SearchData;
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
import java.util.Objects;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private SearchData searchData;
    private ImageView profileImageView;

    private List<Track> tracksList;
    private SearchAdapter searchAdapter;
    private RecyclerView recyclerView;
    private String accessToken = "";

    public SearchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        profileImageView = view.findViewById(R.id.profile_avatar);

        searchData = new SearchData();
        searchView = view.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                performSearch(query);
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Đây là nơi bạn xử lý sự kiện khi người dùng nhập text vào thanh tìm kiếm
                performSearch(newText);
                return false;
            }
        });

        recyclerView = view.findViewById(R.id.searchRecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set layout manager
        tracksList = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(), tracksList);
        recyclerView.setAdapter(searchAdapter);
        loadData();


        return view;
    }

    private void performSearch(String query) {
        List<String> type = new ArrayList<>();
        type.add("track");
        if (Objects.equals(query, "")){
            tracksList.clear();
            searchAdapter.notifyDataSetChanged();
        }
        searchData.searchTracks(accessToken, query, type, 20, 0, "VN", new SearchData.OnTracksLoadedListener() {
            @Override
            public void onTracksLoaded(List<TracksPager> tracks) {
                tracksList.clear();
                if (tracks.get(0).tracks != null) {
                    Log.d("test search", tracks.get(0).tracks.items.get(0).name);
                    Log.d("test search", String.valueOf(tracks.get(0).tracks.items.size()));
                    for (TracksPager tracksPager : tracks) {
                        if (tracksPager.tracks != null && !tracksPager.tracks.items.isEmpty()) {
                            tracksList.addAll(tracksPager.tracks.items);
                        }
                    }
                    searchAdapter.notifyDataSetChanged();

                } else {
                    Log.e("test search", "Track object is null");
                }


            }
            @Override
            public void onFailure(String errorMessage) {
                // Xử lý khi có lỗi xảy ra
                Log.e("Spotify API", "Fail to get search track");

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
                            accessToken = dataSnapshot.child("accessToken").getValue(String.class);
                            String imgURL = dataSnapshot.child("imgURL").getValue(String.class);
                            if (accessToken != null && !accessToken.isEmpty()) {
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

}
