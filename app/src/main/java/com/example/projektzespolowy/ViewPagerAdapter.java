package com.example.projektzespolowy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.lang.reflect.*;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class ViewPagerAdapter  extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    public ViewPager2 vp2;
    public ArrayList<PhotoItem> items;
    public LayoutInflater layoutInflater;


    ViewPagerAdapter(Context context, ArrayList<PhotoItem> data, ViewPager2 viewPager){

        this.vp2 = viewPager;
        this.layoutInflater = LayoutInflater.from(context);
        this.items = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.view_pager_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.dText.setText("Date: " + items.get(position).date);
        displayImage(holder.imgView, items.get(position).path);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;
        TextView dText;


        ViewHolder(View item){
            super(item);

            imgView = item.findViewById(R.id.imageView);
            dText = item.findViewById(R.id.dateText);


        }


    }

    protected void displayImage(ImageView imgView, String imagePath){

        Bitmap imgBitmap = BitmapFactory.decodeFile(imagePath);
        imgView.setImageBitmap(imgBitmap);
    }


}
