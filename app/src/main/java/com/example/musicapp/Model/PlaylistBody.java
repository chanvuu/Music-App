package com.example.musicapp.Model;

import com.google.gson.annotations.SerializedName;

public class PlaylistBody {
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("public")
    private boolean publicc;

    public PlaylistBody() {
    }

    public PlaylistBody(String name, String description, boolean publicc) {
        this.name = name;
        this.description = description;
        this.publicc = publicc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublicc() {
        return publicc;
    }

    public void setPublicc(boolean publicc) {
        this.publicc = publicc;
    }
}
