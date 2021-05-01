package com.example.projektzespolowy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageAdaptor extends BaseAdapter {

    private List<String> galleryPaths;
    private Context mContext;
    private View ImageView;

    public ImageAdaptor ( List < String > galleryPaths, Context mContext ) {
        this.galleryPaths = galleryPaths;
        this.mContext  = mContext;
    }

    protected void displayImage(ImageView imgView, String imagePath){

        Bitmap imgBitmap = BitmapFactory.decodeFile(imagePath);
        imgView.setImageBitmap(imgBitmap);
    }

    @Override
    public int getCount() {
        return galleryPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;

        if(imageView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(50,50));
            imageView.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        }

        //imageView.setImageResource(mThumbIds.get(position));

        displayImage(imageView, galleryPaths.get(position));
        return imageView;
        //return convertView;
    }
}