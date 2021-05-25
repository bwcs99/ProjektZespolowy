package com.example.projektzespolowy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class ImagesDetails extends AppCompatActivity {

    ViewPager2 vp2;
    TagContainerLayout myTagContainerLayout;
    public  ArrayList<PhotoItem> items = new ArrayList<>();
    public String changedTag;

    public int positionFromGridView;

    private String dialogTitle;
    private String dialogMessage;
    private String okButtonText;
    private String cancelButtonText;
    public String currentPositionTag;
    public String tagMapTag;
    public String newMap;

    private final String positionToRestore = "PREV_POSITION";
    private final String listToRestore = "PREV_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_details);

        okButtonText = getString(R.string.okText);
        cancelButtonText = getString(R.string.cancelText);
        currentPositionTag = getString(R.string.currentGridPosition);
        tagMapTag = getString(R.string.MAP_TAG);
        newMap = getString(R.string.newMapTag);

        changedTag = getString(R.string.CHANGED_PARCELABLE);

        dialogTitle = getString(R.string.addingTagsTitle);
        dialogMessage = getString(R.string.addingTagsMessage);

        items = getIntent().getParcelableArrayListExtra(getString(R.string.PARCELABLE_TAG));
        positionFromGridView = getIntent().getIntExtra(currentPositionTag, 0);

        Log.d("Projekt zespołowy: ", "Dostałem: " + positionFromGridView);

        myTagContainerLayout = findViewById(R.id.tagView);

        vp2 = findViewById(R.id.viewPager);

        vp2.setAdapter(new ViewPagerAdapter(this, items, vp2));

        myTagContainerLayout.setTags(items.get(positionFromGridView).tags);


        vp2.setCurrentItem(positionFromGridView, false);


        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){

            @Override
            public void onPageSelected(int position){

                myTagContainerLayout.setTags(items.get(position).tags);
                super.onPageSelected(position);
            }

        });

        myTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

                String tagToRemove = myTagContainerLayout.getTagText(position);

                myTagContainerLayout.removeTag(position);

                int currentPosition = vp2.getCurrentItem();

                items.get(currentPosition).tags.remove(tagToRemove);

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

    }

    private void changeAdapter(){
        vp2.setAdapter(new ViewPagerAdapter(this, items, vp2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.second_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.addTag:
                showAlertDialog();
                return true;
            case R.id.retToMain:
                returnToMain();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void returnToMain(){

        Intent finishIntent = new Intent();

        finishIntent.putParcelableArrayListExtra(changedTag, items);
        setResult(Activity.RESULT_OK, finishIntent);

        finish();

    }

    private void showAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View customView = inflater.inflate(R.layout.alert_dialog, null);

        EditText myTagInput = customView.findViewById(R.id.newTagInput);

        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setView(customView);

        builder.setPositiveButton(okButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String newTag = myTagInput.getText().toString();
                int currentPosition = vp2.getCurrentItem();

                if(!(items.get(currentPosition).tags.contains(newTag))){

                    items.get(currentPosition).tags.add(newTag);
                    myTagContainerLayout.setTags(items.get(currentPosition).tags);

                }
            }
        });

        builder.setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void showDeletionAlertDialog(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.message1))
                .setTitle(getString(R.string.title1));

        builder.setPositiveButton(R.string.yes1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage();
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

    public void deleteImage(){

        if(items.size() <= 0){
            Toast.makeText(this,
                    "There are no images to delete !",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        int currentPosition = vp2.getCurrentItem();

        items.remove(currentPosition);

        changeAdapter();
        vp2.setCurrentItem(currentPosition);

        Toast.makeText(this, "Image successfully deleted !", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        int currentPosition = vp2.getCurrentItem();

        outState.putInt(positionToRestore, currentPosition);
        outState.putParcelableArrayList(listToRestore, items);

        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){

        int savedPosition = savedInstanceState.getInt(positionToRestore);

        vp2.setCurrentItem(savedPosition, false);

        myTagContainerLayout.setTags(items.get(savedPosition).tags);

        items = savedInstanceState.getParcelableArrayList(listToRestore);

        super.onRestoreInstanceState(savedInstanceState);


    }



}