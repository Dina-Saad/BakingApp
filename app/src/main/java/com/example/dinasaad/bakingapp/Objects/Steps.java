package com.example.dinasaad.bakingapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DinaSaad on 14/08/2017.
 */

public class Steps implements Parcelable {
    String Id;
    String ShortDescription;
    String Description;
    String VideoURL;
    String ThumbnailURL;

    public Steps(){

    }
    //constructor
    public Steps(  String Id,
            String ShortDescription,
            String Description,
            String VideoURL,
            String ThumbnailURL)
    {
        this.Id=Id;
        this.ShortDescription=ShortDescription;
        this.Description=Description;
        this.VideoURL=VideoURL;
        this.ThumbnailURL=ThumbnailURL;
    }
    public Steps(Parcel in)
    {
        String[] data = new String[5];
        in.readStringArray(data);

        this.Id=data[0];
        this.ShortDescription=data[1];
        this.Description=data[2];
        this.VideoURL=data[3];
        this.ThumbnailURL=data[4];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.Id,
                this.ShortDescription,
                this.Description,
                this.VideoURL,
                this.ThumbnailURL});
    }
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }
        @Override
        public Steps[] newArray(int size) {
            return new Steps [size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    public String getThumbnailURL() {
        return ThumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        ThumbnailURL = thumbnailURL;
    }
}
