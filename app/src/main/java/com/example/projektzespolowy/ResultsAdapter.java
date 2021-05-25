package com.example.projektzespolowy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder>{

    public ArrayList<PhotoItem> data;
    public Context myContext;

    private LayoutInflater layoutInflater;

    public ResultsAdapter(ArrayList<PhotoItem> dataList, Context context){

        this.data = dataList;
        this.myContext = context;

    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(myContext);
        View listItem = layoutInflater.inflate(R.layout.results_row, parent, false);

        return new ViewHolder(listItem);

    }

    private void displayImage(ImageView imgView, String imagePath){

        Bitmap imgBitmap = BitmapFactory.decodeFile(imagePath);
        imgView.setImageBitmap(imgBitmap);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ViewHolder holder, int position) {

        displayImage(holder.imgView, data.get(position).path);
        holder.dateView.setText(data.get(position).date);
        holder.myTagContainerLayout2.setTags(data.get(position).tags);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgView;
        public TextView dateView;
        public TagContainerLayout myTagContainerLayout2;

        public ViewHolder(View view){

            super(view);
            this.imgView = view.findViewById(R.id.imageView2);
            this.dateView = view.findViewById(R.id.textView);
            this.myTagContainerLayout2 = view.findViewById(R.id.tagView2);


        }
    }
}
