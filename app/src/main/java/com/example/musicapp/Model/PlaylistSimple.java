//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.musicapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.PlaylistTracksInformation;

public class PlaylistSimple extends PlaylistBase {
    public PlaylistTracksInformation tracks;
    public static final Parcelable.Creator<PlaylistSimple> CREATOR = new Parcelable.Creator<PlaylistSimple>() {
        public PlaylistSimple createFromParcel(Parcel source) {
            return new PlaylistSimple(source);
        }

        public PlaylistSimple[] newArray(int size) {
            return new PlaylistSimple[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.tracks, flags);
    }

    public PlaylistSimple() {
    }

    protected PlaylistSimple(Parcel in) {
        super(in);
        this.tracks = (PlaylistTracksInformation)in.readParcelable(PlaylistTracksInformation.class.getClassLoader());
    }
}
