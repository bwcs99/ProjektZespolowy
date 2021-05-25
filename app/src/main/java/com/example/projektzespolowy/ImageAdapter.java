package com.example.projektzespolowy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;
import java.util.zip.Inflater;

public class ImageAdapter extends BaseAdapter {

    private final List<PhotoItem> photoItems;
    private final Context mContext;
    private LayoutInflater myInflater = null;

    public ImageAdapter(List < PhotoItem > photoItems, Context mContext ) {
        this.photoItems = photoItems;
        this.mContext  = mContext;
    }

    protected void displayImage(ImageView imgView, String imagePath){

        Bitmap imgBitmap = BitmapFactory.decodeFile(imagePath);
        imgView.setImageBitmap(imgBitmap);
    }

    @Override
    public int getCount() {
        return photoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return (long) 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View cview = convertView;

        if(myInflater == null){
            myInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(cview == null){

            cview = myInflater.inflate(R.layout.row_layout, null);

        }

        ImageView imgView = (ImageView) cview.findViewById(R.id.imgView);

        displayImage(imgView, photoItems.get(position).getPath().trim());
        return imgView;

    }
}