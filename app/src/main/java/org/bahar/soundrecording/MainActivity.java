package org.bahar.soundrecording;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LOG_MSG";
    ImageButton buttonRecord, buttonStopRecording, buttonPlay, buttonStopPlaying, buttonPause, buttonDelete;
    ListView ShowRecordingList;
    FloatingActionButton myFloatingActionButton;
    TextView aaa;
    private List<String> myList;
    String AudioSavePathInDevice = null;
    String RecordingName= null;
    String RecordingPath,mCurrent;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    File myFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setStartState();
        mCurrent="";
        //loadList(getAlbumStorageDir());
        Bundle EXTRA_MESSAGE=getIntent().getExtras();
        if (EXTRA_MESSAGE != null)
            {   String value = EXTRA_MESSAGE.getString("EXTRA_MESSAGE");
                palySelectedRecord(getAlbumStorageDir().getAbsolutePath(),value);
                EXTRA_MESSAGE=null;
            }


        random = new Random();

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    RecordingName=CreateRandomAudioFileName(6) + "_rec.3gp" ;
                    RecordingPath=getAlbumStorageDir().getAbsolutePath() ;
                    AudioSavePathInDevice = RecordingPath + "/" + RecordingName;
                    mCurrent=AudioSavePathInDevice;

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonRecord.setEnabled(false);
                    buttonRecord.setImageResource(R.drawable.record_rec);
                    buttonStopRecording.setEnabled(true);
                    buttonStopRecording.setImageResource(R.drawable.stop_active);
                    buttonPlay.setEnabled(false);
                    buttonPlay.setImageResource(R.drawable.play);
                    buttonStopPlaying.setEnabled(false);
                    buttonStopPlaying.setImageResource(R.drawable.stop);
                    buttonPause.setEnabled(false);
                    buttonPause.setImageResource(R.drawable.pause);
                    buttonDelete.setEnabled(false);
                    buttonDelete.setImageResource(R.drawable.delete_circle);

                    Toast.makeText(MainActivity.this, "Recording started", Toast.LENGTH_LONG).show();
                }
                else {

                    requestPermission();

                }

            }
        });

        buttonStopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaRecorder.stop();

                buttonRecord.setEnabled(true);
                buttonRecord.setImageResource(R.drawable.record_rec_active);
                buttonStopRecording.setEnabled(false);
                buttonStopRecording.setImageResource(R.drawable.stop);
                buttonPlay.setEnabled(true);
                buttonPlay.setImageResource(R.drawable.play_active);
                buttonStopPlaying.setEnabled(false);
                buttonStopPlaying.setImageResource(R.drawable.stop);
                buttonPause.setEnabled(false);
                buttonPause.setImageResource(R.drawable.pause);
                buttonDelete.setEnabled(true);
                buttonDelete.setImageResource(R.drawable.delete_circle_active);

                //aaa.setText(AudioSavePathInDevice);
                Toast.makeText(MainActivity.this, "Recording Completed", Toast.LENGTH_LONG).show();

            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException {

                buttonRecord.setEnabled(true);
                buttonRecord.setImageResource(R.drawable.record_rec_active);
                buttonStopRecording.setEnabled(false);
                buttonStopRecording.setImageResource(R.drawable.stop);
                buttonPlay.setEnabled(false);
                buttonPlay.setImageResource(R.drawable.play);
                buttonStopPlaying.setEnabled(true);
                buttonStopPlaying.setImageResource(R.drawable.stop_active);
                buttonPause.setEnabled(true);
                buttonPause.setImageResource(R.drawable.pause_active);
                buttonDelete.setEnabled(true);
                buttonDelete.setImageResource(R.drawable.delete_circle_active);

                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(mCurrent);
                    mediaPlayer.prepare();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();

                Toast.makeText(MainActivity.this, "Recording Playing", Toast.LENGTH_LONG).show();

            }
        });

        buttonStopPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonRecord.setEnabled(true);
                buttonRecord.setImageResource(R.drawable.record_rec_active);
                buttonStopRecording.setEnabled(false);
                buttonStopRecording.setImageResource(R.drawable.stop);
                buttonPlay.setEnabled(true);
                buttonPlay.setImageResource(R.drawable.play_active);
                buttonStopPlaying.setEnabled(false);
                buttonStopPlaying.setImageResource(R.drawable.stop);
                buttonPause.setEnabled(false);
                buttonPause.setImageResource(R.drawable.pause);
                buttonDelete.setEnabled(true);
                buttonDelete.setImageResource(R.drawable.delete_circle_active);

                if(mediaPlayer != null){

                    mediaPlayer.stop();
                    mediaPlayer.release();

                    MediaRecorderReady();

                }

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonRecord.setEnabled(true);
                buttonRecord.setImageResource(R.drawable.record_rec_active);
                buttonStopRecording.setEnabled(false);
                buttonStopRecording.setImageResource(R.drawable.stop);
                buttonPlay.setEnabled(false);
                buttonPlay.setImageResource(R.drawable.play);
                buttonStopPlaying.setEnabled(false);
                buttonStopPlaying.setImageResource(R.drawable.stop);
                buttonPause.setEnabled(false);
                buttonPause.setImageResource(R.drawable.pause);
                buttonDelete.setEnabled(false);
                buttonDelete.setImageResource(R.drawable.delete_circle);

                if(mediaPlayer != null){

                    File file = new File(mCurrent);
                    boolean deleted = file.delete();

                }

            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonRecord.setEnabled(true);
                buttonRecord.setImageResource(R.drawable.record_rec_active);
                buttonStopRecording.setEnabled(false);
                buttonStopRecording.setImageResource(R.drawable.stop);
                buttonPlay.setEnabled(true);
                buttonPlay.setImageResource(R.drawable.play_active);
                buttonStopPlaying.setEnabled(true);
                buttonStopPlaying.setImageResource(R.drawable.stop_active);
                buttonPause.setEnabled(false);
                buttonPause.setImageResource(R.drawable.pause);
                buttonDelete.setEnabled(true);
                buttonDelete.setImageResource(R.drawable.delete_circle_active);

                if(mediaPlayer != null){

                    mediaPlayer.pause();

                    MediaRecorderReady();

                }

            }
        });

        myFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ListActivity.class);
               // myIntent.putExtra("key", value);
                startActivity(myIntent);
                }
            });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {

                        Toast.makeText(MainActivity.this, "Permission is Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Permission is Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    public void MediaRecorderReady(){

        mediaRecorder=new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        mediaRecorder.setOutputFile(AudioSavePathInDevice);

    }

    public File getAlbumStorageDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), "MyRecordings");
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }


    //not used yet
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public String CreateRandomAudioFileName(int string){

        StringBuilder stringBuilder = new StringBuilder( string );

        int i = 0 ;
        while(i < string ) {

            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();

    }

    public void findViews(){

        buttonRecord = findViewById(R.id.activity_main_image_button_record);
        buttonStopRecording = findViewById(R.id.activity_main_image_button_stop_recording);
        buttonPlay = findViewById(R.id.activity_main_image_button_play);
        buttonStopPlaying = findViewById(R.id.activity_main_image_button_stop_playing);
        buttonPause= findViewById(R.id.activity_main_image_button_pause);
        buttonDelete= findViewById(R.id.activity_main_image_button_delete);

        myFloatingActionButton=findViewById(R.id.floatingActionButton);

        ShowRecordingList= findViewById(R.id.showList);

        aaa = findViewById(R.id.fileNumber);
    }

    public void setStartState(){
        buttonRecord.setEnabled(true);
        buttonRecord.setImageResource(R.drawable.record_rec_active);
        buttonStopRecording.setEnabled(false);
        buttonStopRecording.setImageResource(R.drawable.stop);
        buttonPlay.setEnabled(false);
        buttonPlay.setImageResource(R.drawable.play);
        buttonStopPlaying.setEnabled(false);
        buttonStopPlaying.setImageResource(R.drawable.stop);
        buttonPause.setEnabled(false);
        buttonPause.setImageResource(R.drawable.pause);
        buttonDelete.setEnabled(false);
        buttonDelete.setImageResource(R.drawable.delete_circle);
    }

    public void palySelectedRecord(String filePath, String fileName){
        buttonRecord.setEnabled(true);
        buttonRecord.setImageResource(R.drawable.record_rec_active);
        buttonStopRecording.setEnabled(false);
        buttonStopRecording.setImageResource(R.drawable.stop);
        buttonPlay.setEnabled(false);
        buttonPlay.setImageResource(R.drawable.play);
        buttonStopPlaying.setEnabled(true);
        buttonStopPlaying.setImageResource(R.drawable.stop_active);
        buttonPause.setEnabled(true);
        buttonPause.setImageResource(R.drawable.pause_active);
        buttonDelete.setEnabled(true);
        buttonDelete.setImageResource(R.drawable.delete_circle_active);

        mediaPlayer = new MediaPlayer();

        try {
            mCurrent=filePath+"/"+fileName;
            mediaPlayer.setDataSource(mCurrent);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    public void loadList(File MyDirectory){
        myList = new ArrayList<String>();

        myFile = new File( MyDirectory.getAbsolutePath() );
        File recordlist[] = myFile.listFiles();

        for( int i=0; i< recordlist.length; i++)
        {
            myList.add( recordlist[i].getName() );
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_list_view, R.id.voice_list_item, myList);
        ShowRecordingList.setAdapter(adapter); //Set all the file in the list.
    }
    }


