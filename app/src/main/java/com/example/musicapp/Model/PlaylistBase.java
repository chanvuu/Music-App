//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.musicapp.Model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.UserPublic;

public abstract class PlaylistBase implements Parcelable {
    public Boolean collaborative;
    public String description;
    public Map<String, String> external_urls;
    public String href;
    public String id;
    public List<Image> images;
    public String name;
    public UserPublic owner;
    @SerializedName("public")
    public Boolean is_public;
    public String snapshot_id;
    public String type;
    public String uri;

    protected PlaylistBase() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.collaborative);
        dest.writeValue(this.description);
        dest.writeMap(this.external_urls);
        dest.writeValue(this.href);
        dest.writeValue(this.id);
        dest.writeTypedList(this.images);
        dest.writeValue(this.name);
        dest.writeParcelable(this.owner, flags);
        dest.writeValue(this.is_public);
        dest.writeValue(this.snapshot_id);
        dest.writeValue(this.type);
        dest.writeValue(this.uri);
    }

    protected PlaylistBase(Parcel in) {
        this.collaborative = (Boolean)in.readValue(Boolean.class.getClassLoader());
        this.description = (String)in.readValue(String.class.getClassLoader());
        this.external_urls = in.readHashMap(Map.class.getClassLoader());
        this.href = (String)in.readValue(String.class.getClassLoader());
        this.id = (String)in.readValue(String.class.getClassLoader());
        this.images = in.createTypedArrayList(Image.CREATOR);
        this.name = (String)in.readValue(String.class.getClassLoader());
        this.owner = (UserPublic)in.readParcelable(UserPublic.class.getClassLoader());
        this.is_public = (Boolean)in.readValue(Boolean.class.getClassLoader());
        this.snapshot_id = (String)in.readValue(String.class.getClassLoader());
        this.type = (String)in.readValue(String.class.getClassLoader());
        this.uri = (String)in.readValue(String.class.getClassLoader());
    }
}
