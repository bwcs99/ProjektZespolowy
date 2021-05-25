package com.example.projektzespolowy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    public ArrayList<PhotoItem> filteredPhotoItems;
    public RecyclerView resultsRecyclerView;
    public ResultsAdapter myResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        filteredPhotoItems = getIntent().getParcelableArrayListExtra("FilteredItems");

        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);

        myResultsAdapter = new ResultsAdapter(filteredPhotoItems, this);

        resultsRecyclerView.setAdapter(myResultsAdapter);

        LinearLayoutManager linMen = new LinearLayoutManager(this);
        resultsRecyclerView.setLayoutManager(linMen);

    }

    public void returnToPrevious(View view){

        Intent finishIntent = new Intent();
        finish();
    }

}