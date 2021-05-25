package com.example.projektzespolowy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    public ArrayList<PhotoItem> itemsFromMain;
    public ArrayList<String> tagsToDisplay;
    private RecyclerView myRecyclerView;
    public TagAdapter myTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        myRecyclerView = findViewById(R.id.myTagRecyclerView);

        tagsToDisplay = getIntent().getStringArrayListExtra(getString(R.string.hints));
        itemsFromMain = getIntent().getParcelableArrayListExtra("items");

        myTagAdapter = new TagAdapter(tagsToDisplay, this);

        myRecyclerView.setAdapter(myTagAdapter);
        LinearLayoutManager linMen = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(linMen);

    }

    private ArrayList<PhotoItem> filterGalleryByTags(ArrayList<String> validElements){

        Predicate<PhotoItem> byTagList = pi -> pi.tags.containsAll(validElements);

        ArrayList<PhotoItem> result = (ArrayList<PhotoItem>) itemsFromMain.stream()
                .filter(byTagList)
                .collect(Collectors.toList());

        return result;

    }

    public void toResultActivity(View view){

        ArrayList<String> selectedTags = myTagAdapter.selectedTags;

        if(selectedTags.size() == 0){
            Toast.makeText(this, "No tags are selected !", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<PhotoItem> result = filterGalleryByTags(selectedTags);

        Intent newIntent = new Intent(this, ResultsActivity.class);
        newIntent.putParcelableArrayListExtra("FilteredItems", result);
        startActivity(newIntent);


    }

    public void backToGallery(View v){

        Intent finishIntent = new Intent();
        finish();
    }

}
