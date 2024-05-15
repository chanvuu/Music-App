package com.example.musicapp.Model;

import com.google.gson.annotations.SerializedName;

public class AddTrackRequest {
    @SerializedName("uris")
    private String[] trackUri;
    @SerializedName("position")
    private int position;

    public AddTrackRequest(String[] trackUri, int position) {
        this.trackUri = trackUri;
        this.position = position;
    }

    public String[] getTrackUri() {
        return trackUri;
    }

    public void setTrackUri(String[] trackUri) {
        this.trackUri = trackUri;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

