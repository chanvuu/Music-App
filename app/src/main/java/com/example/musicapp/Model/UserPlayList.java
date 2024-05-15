package com.example.musicapp.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserPlayList {
    @SerializedName("items")
    public List<PlaylistItem> items;

    public static class PlaylistItem {
        @SerializedName("collaborative")
        public boolean collaborative;

        @SerializedName("description")
        public String description;

        @SerializedName("external_urls")
        public ExternalUrl externalUrls;

        @SerializedName("id")
        public String id;

        @SerializedName("images")
        public List<Image> images;

        @SerializedName("name")
        public String name;

        @SerializedName("owner")
        public PublicUser owner;

        @SerializedName("public")
        public Boolean isPublic;

        @SerializedName("snapshot_id")
        public String snapshotId;

        @SerializedName("type")
        public String type;

        @SerializedName("uri")
        public String uri;

        @SerializedName("tracks")
        public PlaylistTracksRef tracks;

        public boolean isCollaborative() {
            return collaborative;
        }

        public void setCollaborative(boolean collaborative) {
            this.collaborative = collaborative;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public ExternalUrl getExternalUrls() {
            return externalUrls;
        }

        public void setExternalUrls(ExternalUrl externalUrls) {
            this.externalUrls = externalUrls;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PublicUser getOwner() {
            return owner;
        }

        public void setOwner(PublicUser owner) {
            this.owner = owner;
        }

        public Boolean getPublic() {
            return isPublic;
        }

        public void setPublic(Boolean aPublic) {
            isPublic = aPublic;
        }

        public String getSnapshotId() {
            return snapshotId;
        }

        public void setSnapshotId(String snapshotId) {
            this.snapshotId = snapshotId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public PlaylistTracksRef getTracks() {
            return tracks;
        }

        public void setTracks(PlaylistTracksRef tracks) {
            this.tracks = tracks;
        }

    }
}
