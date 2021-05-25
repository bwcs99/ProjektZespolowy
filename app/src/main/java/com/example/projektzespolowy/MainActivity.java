package com.example.projektzespolowy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public ArrayList<PhotoItem> items = new ArrayList<>();
    private boolean isOrderReversed = false;

    public GridView gridView = null;


    private final String ARR_TAG = "ITEMS_ARRAY";
    private final String ORDER_TAG = "CURRENT_ORDER";

    public String changedParcelable;
    public String mapTag;
    public String gridPositionTag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changedParcelable = getString(R.string.CHANGED_PARCELABLE);
        mapTag = getString(R.string.MAP_TAG);
        gridPositionTag = getString(R.string.currentGridPosition);

        gridView = findViewById(R.id.myGrid);

        loadImageData();

        if(items.size() != 0){
            sortItems(false);
        }


        gridView.setAdapter(new ImageAdapter(items, this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick ( AdapterView < ? > parent, View view, int position, long id ) {

                Intent intent = new Intent(getApplicationContext(), ImagesDetails.class);

                intent.putParcelableArrayListExtra(getString(R.string.PARCELABLE_TAG), items);
                intent.putExtra(gridPositionTag, position);
                startActivityForResult(intent, 2);


            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showAlertDialog(position);
                return true;
            }
        });

    }


    private ArrayList<String> prepareTagsHintsList(){

        ArrayList<String> usefulTags = new ArrayList<String>();
        ArrayList<String> photoTags;

        for(PhotoItem item : items){

            photoTags = item.tags;

            for(String tag : photoTags){

                if(usefulTags.contains(tag)){

                    continue;

                } else {

                    usefulTags.add(tag);

                }
            }

        }

        return usefulTags;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.first_menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.sort:
                sortItems(true);
                return true;
            case R.id.takephoto:
                startPhotoActivity();
                return true;
            case R.id.search:
                openSearchActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void saveImageData(ArrayList<PhotoItem> list){

        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences(getString(R.string.imagesData), MODE_PRIVATE);
        String listJSON = gson.toJson(list);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(getString(R.string.savedImages), listJSON);
        editor.apply();

    }

    private void openSearchActivity(){

        String hintsTag = getString(R.string.hints);
        ArrayList<String> tagListToDisplay = prepareTagsHintsList();
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putStringArrayListExtra(hintsTag, tagListToDisplay);
        intent.putParcelableArrayListExtra("items", items);

        startActivity(intent);

    }


    public void loadImageData(){

        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences(getString(R.string.imagesData), MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<PhotoItem>>() {}.getType();

        String myJSON = sp.getString(getString(R.string.savedImages), null);

        if(myJSON != null){
            items = gson.fromJson(myJSON, type);
        }  else {
            items = new ArrayList<PhotoItem>();
        }

    }


    private void showAlertDialog(int position){

        final boolean[] isDeleted = {false};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.message1)
                .setTitle(R.string.title1);

        builder.setPositiveButton(R.string.yes1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File fileToDelete = new File(items.get(position).getPath().trim());
                isDeleted[0] = fileToDelete.delete();
                items.remove(position);

                if(isDeleted[0]){

                    showInfo();
                    saveImageData(items);
                    changeAdapter();
                }
            }
        });

        builder.setNegativeButton(R.string.no1, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                return;

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showInfo(){

        Toast.makeText(this,
                "Image has been successfully deleted !",
                Toast.LENGTH_SHORT).show();
    }

    private void changeAdapter(){
        gridView.setAdapter(new ImageAdapter(items, this));
    }

    public void sortItems(boolean scrollToValidPosition){

        int currPosition = 0;
        long pID = 0;

        if(scrollToValidPosition){

             currPosition = gridView.getFirstVisiblePosition();
             pID = items.get(currPosition).photoID;

        }

        if(isOrderReversed){

            Collections.sort(items);
            isOrderReversed = false;
            changeAdapter();

        } else {

            Collections.sort(items, Collections.reverseOrder());
            isOrderReversed = true;
            changeAdapter();
        }

        if(scrollToValidPosition){

            int newPosition = searchForPhotoWithGivenID(pID);
            gridView.smoothScrollToPosition(newPosition);

        }


    }

    private int searchForPhotoWithGivenID(long id){

        int position = 0;

        for(PhotoItem i : items){
            if(i.photoID == id){

                break;
            }

            position++;
        }

        return position;
    }


    public void startPhotoActivity(){

        Intent photoIntent = new Intent(this, PhotoActivity.class);
        startActivityForResult(photoIntent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        String path = null;

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == 1){

                ArrayList<PhotoItem> temp =  null;

                String PATH_TAG = "PATHS_TO_FILES";
                temp = data.getParcelableArrayListExtra(PATH_TAG);

               if(temp != null){

                   items.addAll(temp);

               }

               saveImageData(items);


               if(gridView != null){

                   if(isOrderReversed){
                       isOrderReversed = false;
                   } else {
                       isOrderReversed = true;
                   }

                   sortItems(false);
                   changeAdapter();
               }


            } else if(requestCode == 2){

                ArrayList<PhotoItem> temporaryArray = null;

                temporaryArray = data.getParcelableArrayListExtra(changedParcelable);

                if(temporaryArray != null && temporaryArray != items){

                    items = temporaryArray;
                    saveImageData(items);


                    if(gridView != null){
                        changeAdapter();
                    }

                }



            }
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        int currentGridPosition = gridView.getFirstVisiblePosition();

        outState.putInt("CURRENT_POSITION", currentGridPosition);
        outState.putParcelableArrayList(ARR_TAG, items);
        outState.putBoolean(ORDER_TAG, isOrderReversed);

        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        int returnToPosition = savedInstanceState.getInt("CURRENT_POSITION", 0);
        items = savedInstanceState.getParcelableArrayList(ARR_TAG);
        isOrderReversed = savedInstanceState.getBoolean(ORDER_TAG);

        if(isOrderReversed){
            isOrderReversed = false;
        } else{
            isOrderReversed = true;
        }

       gridView.smoothScrollToPosition(returnToPosition);

       sortItems(false);

    }
}