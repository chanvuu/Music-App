//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.musicapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Pager;

public class FeaturedPlaylists implements Parcelable {
    public String message;
    public Pager<PlaylistSimple> playlists;
    public static final Parcelable.Creator<FeaturedPlaylists> CREATOR = new Parcelable.Creator<FeaturedPlaylists>() {
        public FeaturedPlaylists createFromParcel(Parcel source) {
            return new FeaturedPlaylists(source);
        }

        public FeaturedPlaylists[] newArray(int size) {
            return new FeaturedPlaylists[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeParcelable(this.playlists, 0);
    }

    public FeaturedPlaylists() {
    }

    protected FeaturedPlaylists(Parcel in) {
        this.message = in.readString();
        this.playlists = (Pager)in.readParcelable(Pager.class.getClassLoader());
    }
}
