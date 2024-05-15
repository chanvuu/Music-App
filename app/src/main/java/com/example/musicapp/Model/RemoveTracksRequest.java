package com.example.musicapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import kaaes.spotify.webapi.android.models.TracksToRemove;

public class RemoveTracksRequest {
    @SerializedName("tracks")
    public List<TrackUri> tracks;

    public RemoveTracksRequest() {
    }

    public RemoveTracksRequest(List<TrackUri> tracks) {
        this.tracks = tracks;
    }

    public static class TrackUri {
        @SerializedName("uri")
        public String uri;

        public TrackUri(String uri) {
            this.uri = uri;
        }
    }
}


