package com.example.musicapp.Model;

import com.google.gson.annotations.SerializedName;

public class EditPlayListRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("public")

    private boolean isPublic;
    @SerializedName("description")
    private String description;
    @SerializedName("collaborative")
    private boolean collaborative;

    public EditPlayListRequest(String name, boolean isPublic, String description, boolean collaborative) {
        this.name = name;
        this.isPublic = isPublic;
        this.description = description;
        this.collaborative = collaborative;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }
}
