package fire.hacker.risha.musicapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{

    static MediaPlayer mp,bacgroudMusic;
    ArrayList <File> mySongs;
    static SeekBar sb,mVolumeControl,mVolumeControl1;
    ImageButton btFF,btFB,btNxt,btPv;
    ToggleButton btPlay;
    static int position;
    Uri u;
    Thread updateSeekBar;
    TextView songName,runtime;
    private double startTime = 0;
    private double finalTime = 0;
    Timer timer;
    static int theme=0;
    RelativeLayout vi;
    AudioManager mAudioManager,mAudioManager1;
    private  static int MAX_VOLUME = 100;
    TextView vol2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        btPlay = (ToggleButton)findViewById(R.id.btPlay);
        btFF = (ImageButton)findViewById(R.id.btFF);
        btFB = (ImageButton)findViewById(R.id.btFB);
        btNxt = (ImageButton)findViewById(R.id.btNxt);
        btPv = (ImageButton)findViewById(R.id.btPv);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        sb = (SeekBar)findViewById(R.id.seekbar);
        songName = (TextView)findViewById(R.id.SongName);
        runtime = (TextView)findViewById(R.id.runtime);
        vi = (RelativeLayout)findViewById(R.id.main);
        //mVolumeControl1.setVisibility(View.INVISIBLE);
        vol2 = (TextView)findViewById(R.id.vol2);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        mVolumeControl = (SeekBar)findViewById(R.id.volume_control);
        mVolumeControl1 = (SeekBar)findViewById(R.id.volume_control1);

        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mAudioManager1 = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        mVolumeControl.setMax(MAX_VOLUME);
        mVolumeControl1.setMax(MAX_VOLUME);

        mVolumeControl.setProgress(MAX_VOLUME*mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)/mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mVolumeControl1.setProgress(MAX_VOLUME*mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)/mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));


        SeekBar.OnSeekBarChangeListener mVolumeControlChangeListener = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float volume = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                bacgroudMusic.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };


        SeekBar.OnSeekBarChangeListener mVolumeControlChangeListener1 = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float volume = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                mp.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };

        mVolumeControl.setOnSeekBarChangeListener(mVolumeControlChangeListener);
        mVolumeControl1.setOnSeekBarChangeListener(mVolumeControlChangeListener1);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(theme==0){
            vi.setBackgroundResource(R.drawable.mm);
        }
        else if(theme==1){
            vi.setBackgroundResource(R.drawable.img1);
        }
        else if(theme==2){
            vi.setBackgroundResource(R.drawable.img2);
        }
        else if(theme==3){
            vi.setBackgroundResource(R.drawable.img3);
        }
        else if(theme==4){
            vi.setBackgroundResource(R.drawable.img4);
        }
        else if(theme==5){

        }
        else if(theme==6){
            vi.setBackgroundResource(R.drawable.img6);
        }
        else if(theme==7){
            vi.setBackgroundResource(R.drawable.img7);
        }



        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                while(currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                        double timeRemaining = totalDuration - currentPosition;

                        String min1="",sec1="";
                        int min =  (int)TimeUnit.MILLISECONDS.toMinutes((long) currentPosition);
                        int sec = (int)(TimeUnit.MILLISECONDS.toSeconds((long) currentPosition)-TimeUnit.MILLISECONDS.toMinutes((long) currentPosition)*60);
                        if(min<=9){
                            min1 = "0"+min;
                        }
                        else{
                            min1 = min+"";
                        }
                        if(sec<=9){
                            sec1 = "0"+sec;
                        }
                        else{
                            sec1 = sec+"";
                        };
                        runtime.setText(min1+":"+sec1);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

            }
        };

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList)b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);

        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);

        mp.start();
        songName.setText(MainActivity.items[position]);
        sb.setMax(mp.getDuration());

        finalTime = mp.getDuration();
        startTime = mp.getCurrentPosition();
        updateSeekBar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });


    }
///////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.search:
                String url = "http://www.google.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            // action with ID action_settings was selected
            case R.id.about:
                try {
                    startActivity(new Intent(PlayerActivity.this, AboutApp.class).putExtra("pos", position).putExtra("songlist", mySongs));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            default:
                break;
        }

        return true;
    }
    public void goBack(View v){

        mVolumeControl.setVisibility(View.VISIBLE);
        vol2.setVisibility(View.VISIBLE);

        mVolumeControl.setMax(MAX_VOLUME);
        mVolumeControl.setProgress(MAX_VOLUME);

        if(theme==0){
            vi.setBackgroundResource(R.drawable.img7);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song7);
            theme+=7;
        }
        else if(theme==1){
            vi.setBackgroundResource(R.drawable.mm);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                //bacgroudMusic.release();
            }
            theme--;
        }
        else if(theme==2){
            vi.setBackgroundResource(R.drawable.img1);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song1);
            theme--;
        }
        else if(theme==3){
            vi.setBackgroundResource(R.drawable.img2);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song2);
            theme--;
        }
        else if(theme==4){
            vi.setBackgroundResource(R.drawable.img3);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song3);
            theme--;
        }
        else if(theme==5){
            vi.setBackgroundResource(R.drawable.img4);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song4);
            theme--;
        }
        else if(theme==6){
            vi.setBackgroundResource(R.drawable.img5);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song5);
            theme--;
        }
        else if(theme==7){
            vi.setBackgroundResource(R.drawable.img6);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song6);
            theme--;
        }

        if(bacgroudMusic!=null){
            try {
                bacgroudMusic.start();
                bacgroudMusic.setLooping(true);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{

        }

    }

    public void goForward(View v){

        mVolumeControl.setVisibility(View.VISIBLE);
        vol2.setVisibility(View.VISIBLE);


        mVolumeControl.setMax(MAX_VOLUME);
        mVolumeControl.setProgress(MAX_VOLUME);

        if(theme==0){
            vi.setBackgroundResource(R.drawable.img1);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song1);
            theme++;
        }
        else if(theme==1){
            vi.setBackgroundResource(R.drawable.img2);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song2);
            theme++;
        }
        else if(theme==2){
            vi.setBackgroundResource(R.drawable.img3);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song3);
            theme++;
        }
        else if(theme==3){
            vi.setBackgroundResource(R.drawable.img4);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song4);
            theme++;
        }
        else if(theme==4){
            vi.setBackgroundResource(R.drawable.img5);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song5);
            theme++;
        }
        else if(theme==5){
            vi.setBackgroundResource(R.drawable.img6);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song6);
            theme++;
        }
        else if(theme==6){
            vi.setBackgroundResource(R.drawable.img7);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                bacgroudMusic.release();
            }
            bacgroudMusic = MediaPlayer.create(PlayerActivity.this, R.raw.song7);
            theme++;
        }
        else if(theme==7){
            vi.setBackgroundResource(R.drawable.mm);
            if(bacgroudMusic!=null){
                bacgroudMusic.stop();
                //bacgroudMusic.release();
            }
            theme-=7;
        }

        if(bacgroudMusic!=null){

            try {
                bacgroudMusic.start();
                bacgroudMusic.setLooping(true);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{

        }
    }

    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        onSongCompletion();
    }

    public void onSongCompletion(){

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer song) {
                mp.reset();
                startActivity(new Intent(PlayerActivity.this, MainActivity.class));

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btPlay:
                if(mp.isPlaying()){
                    mp.pause();
                }
                else{
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position + 1) % mySongs.size();
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                mVolumeControl1.setProgress(MAX_VOLUME);
                songName.setText(MainActivity.items[position]);
                sb.setMax(mp.getDuration());
                btPlay.setChecked(false);
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position = (position-1<0)?mySongs.size() - 2 : position - 1;
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                mVolumeControl1.setProgress(MAX_VOLUME);
                btPlay.setChecked(false);
                songName.setText(MainActivity.items[position]);
                sb.setMax(mp.getDuration());

                break;
        }
    }
}