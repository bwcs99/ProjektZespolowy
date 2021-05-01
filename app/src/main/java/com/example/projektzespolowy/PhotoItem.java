package com.example.projektzespolowy;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoItem implements Parcelable {

    public long photoID;
    public String timeStamp;
    public String path;
    public ArrayList<String> tags; // tutaj bardziej pasuje set !!

    public PhotoItem(String timeStamp, String path, long photoID){

        this.timeStamp = timeStamp;
        this.path  = path;
        this.photoID = photoID;
    }

    protected PhotoItem(Parcel in) {
        photoID = in.readLong();
        timeStamp = in.readString();
        path = in.readString();
        tags = in.createStringArrayList();
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        @Override
        public PhotoItem createFromParcel(Parcel in) {
            return new PhotoItem(in);
        }

        @Override
        public PhotoItem[] newArray(int size) {
            return new PhotoItem[size];
        }
    };

    public long getPhotoID(){
        return this.photoID;
    }

    public String getTimeStamp(){
        return this.timeStamp;
    }

    public String getPath(){
        return this.path;
    }

    public ArrayList<String> getTags(){
        return this.tags;
    }

    public void setTimeStamp(String newTimeStamp){
        this.timeStamp = newTimeStamp;
    }

    public void setPath(String newPath){
        this.path = newPath;
    }

    public void setPhotoID(long newID){
        this.photoID = newID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(photoID);
        dest.writeString(timeStamp);
        dest.writeString(path);
        dest.writeStringList(tags);
    }
}




