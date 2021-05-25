package com.example.projektzespolowy;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{

    public ArrayList<String> tagsToDisplay;
    public Context myContext;
    public ArrayList<String> selectedTags = new ArrayList<>();

    private LayoutInflater layoutInflater;

    public TagAdapter(ArrayList<String> tagList, Context context){

        this.tagsToDisplay = tagList;
        this.myContext = context;

    }

    @NonNull
    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(myContext);
        View listItem = layoutInflater.inflate(R.layout.tag_row_layout, parent, false);

        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.ViewHolder holder, int position) {

        holder.tagDisplay.setText(tagsToDisplay.get(position));

        holder.cbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = holder.cbox.isChecked();

                if(checked){
                    selectedTags.add(tagsToDisplay.get(position));
                } else{
                    selectedTags.remove(tagsToDisplay.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return tagsToDisplay.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tagDisplay;
        public CheckBox cbox;

        public ViewHolder(View view){

            super(view);
            this.tagDisplay = view.findViewById(R.id.tagDisplayView);
            this.cbox = view.findViewById(R.id.checkBox);


        }
    }
}
