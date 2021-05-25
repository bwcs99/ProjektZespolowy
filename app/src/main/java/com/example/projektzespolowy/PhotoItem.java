package com.example.projektzespolowy;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class PhotoItem implements Parcelable, Comparable<PhotoItem> {


    public long photoID;
    public String date;
    public String path;
    public ArrayList<String> tags;

    public PhotoItem(String date, String path, long photoID){

        this.date = date;
        this.path  = path;
        this.photoID = photoID;
        this.tags = new ArrayList<>();

    }


    protected PhotoItem(Parcel in) {
        photoID = in.readLong();
        date = in.readString();
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

    public String getDate(){
        return this.date;
    }

    public String getPath(){
        return this.path;
    }

    public ArrayList<String> getTags(){
        return this.tags;
    }

    public void removeTag(String toRemove) { this.tags.remove(toRemove); }

    public void addTag(String newTag) { if(this.tags.contains(newTag)) {
    } else this.tags.add(newTag); }

    public void setDate(String newDate){
        this.date = newDate;
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
        dest.writeString(date);
        dest.writeString(path);
        dest.writeStringList(tags);
    }

    @Override
    public int compareTo(PhotoItem o) {
        return this.getDate().compareTo(o.getDate());
    }
}












