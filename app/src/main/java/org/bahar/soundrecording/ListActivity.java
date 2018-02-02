package org.bahar.soundrecording;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity {
    private static final String LOG_TAG ="Warning: " ;
    String message;
    File myFile;
    ListView ShowRecordingList;
    private List<String> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        findViews();
        myList = new ArrayList<>();
        myFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "MyRecordings");

        if (!myFile.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
//            Intent myintent = new Intent(ListActivity.this, MainActivity.class);
//            message="";
//            myintent.putExtra("EXTRA_MESSAGE", message);
//            startActivity(myintent);
        }

        final File thelist[] = myFile.listFiles();

        if (thelist.length==0) {
            Log.e(LOG_TAG, "There is no recorded  file");
//            Intent myintent = new Intent(ListActivity.this, MainActivity.class);
//            message="";
//            myintent.putExtra("EXTRA_MESSAGE", message);
//            startActivity(myintent);
        }

        for( int i=0; i< thelist.length; i++)
            {
                myList.add( thelist[i].getName() );
            }
            Toast.makeText(ListActivity.this, "Total Number of recorded files: "+thelist.length , Toast.LENGTH_LONG).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_list_view, R.id.voice_list_item, myList);
            ShowRecordingList.setAdapter(adapter); //Set all the file in the list.


        ShowRecordingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Toast.makeText(ListActivity.this, "Selected File: "+ thelist[position].getName(), Toast.LENGTH_LONG).show();
                Intent myintent = new Intent(ListActivity.this, MainActivity.class);
                message = thelist[position].getName();
                myintent.putExtra("EXTRA_MESSAGE", message);
                startActivity(myintent);
            }
        });



    }

    private void findViews(){
        ShowRecordingList=findViewById(R.id.showList);
    }

    }

