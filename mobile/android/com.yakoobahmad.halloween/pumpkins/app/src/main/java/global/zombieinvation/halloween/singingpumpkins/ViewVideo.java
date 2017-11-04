package global.zombieinvation.halloween.singingpumpkins;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.google.gson.Gson;


import android.media.MediaPlayer.OnTimedTextListener;
import android.media.MediaPlayer.TrackInfo;
import android.media.TimedText;

import static android.media.AudioManager.ADJUST_LOWER;
import static android.media.AudioManager.ADJUST_RAISE;
import static java.lang.Thread.sleep;

public class ViewVideo extends Activity implements MqttCallback, OnTimedTextListener {

    private static final int INSERT_ID = Menu.FIRST;
    private static Handler handler = new Handler();
    private static final String TAG = "TimedTextTest";

    private static int vid = 0;
    private static int sub = 0;
    private static Video video;

    MqttClient client;

    VideoView vv = null;

    MediaPlayer mediaPlayer = null;


    private static Context mContext;

    public static ViewVideo instace;


    public ViewVideo() throws MqttException {


        // configure messaging client
        resetMqttClient();
        client = new MqttClient("tcp://192.168.20.114:1883", MqttClient.generateClientId(), null);
        client.setCallback(this);
        MqttConnectOptions options = new MqttConnectOptions();

        // connect to message broker
        try {
            client.connect(options);
        } catch (MqttException e) {
            Log.d(getClass().getCanonicalName(), "Connection attempt failed with reason code = " + e.getReasonCode() + ":" + e.getCause());
        }

        // subscribe to app topic
        try {
            client.subscribe("halloween/video");
            // client.subscribe("#");
        } catch (MqttException e) {
            Log.d(getClass().getCanonicalName(), "Subscribe failed with reason code = " + e.getReasonCode());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        new MyDeviceAdminReceiver().onEnabled(getApplicationContext(), new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN));

        vv = new VideoView(getApplicationContext());

        setContentView(vv);

        try {
            playWoods();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        mContext = getApplicationContext();
        instace = this;

        startService(new Intent(this, StickyService.class));

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, ViewVideo.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0,"FullScreen");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createNote();
        }
        return true;
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.d(getClass().getCanonicalName(), "MQTT Server connection lost" + cause);

        try {
            sleep(10000);
            client.connect(new MqttConnectOptions());
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {

        /**
         *
         *  http://localhost:8080/halloween/test/test?m={'command':'Play','name':'KIDNAP_SANDY_CLAWS'}
         *  http://localhost:8080/halloween/test/test?m={'command':'Pause'}
         */

        try {

            video = new Gson().fromJson(message.toString(), Video.class);

            Log.d("HalloweenVideoPlayer", "command:" + video.getCommand() + " | song:" + video.getName());

            if (video.getCommand().equals("Play")) {

                if (video.getName().equals(Video.Name.WOODS)) {
                    vid = R.raw.halloween_woods;
                    sub = R.raw.halloween_woods_sub;
                } else if (video.getName().equals(Video.Name.GRIM_GRINNING_GHOST)) {
                    vid = R.raw.halloween_ggg;
                    sub = R.raw.halloween_ggg_sub;
                } else if (video.getName().equals(Video.Name.THIS_IS_HALLOWEEN)) {
                    vid = R.raw.halloween_tih;
                    sub = R.raw.halloween_tih_sub;
                } else if (video.getName().equals(Video.Name.WHATS_THIS)) {
                    vid = R.raw.halloween_wt;
                    sub = R.raw.halloween_wt_sub;
                } else if (video.getName().equals(Video.Name.KIDNAP_SANDY_CLAWS)) {
                    vid = R.raw.halloween_knsc;
                    sub = R.raw.halloween_knsc_sub;
                } else if (video.getName().equals(Video.Name.MONSTER_MASH)) {
                    vid = R.raw.halloween_mm;
                    sub = R.raw.halloween_mm_sub;
                } else if (video.getName().equals(Video.Name.OOGIE_BOOGIE_PUMPKINS)) {
                    vid = R.raw.halloween_obp;
                    sub = R.raw.halloween_obp_sub;
                } else if (video.getName().equals(Video.Name.TIMEWARP)) {
                    vid = R.raw.halloween_timewarp;
                    sub = R.raw.halloween_timewarp_sub;
                } else if (video.getName().equals(Video.Name.THRILLER)) {
                    vid = R.raw.halloween_thriller;
                    sub = R.raw.halloween_timewarp_sub;
                } else if (video.getName().equals(Video.Name.GHOSTBUSTERS)) {
                    vid = R.raw.halloween_ghostbusters;
                    sub = R.raw.halloween_timewarp_sub;

                } else {

                    return;

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        vv.resume();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + vid));
                                vv.requestFocus();
                                vv.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer pMp) {
                                        mediaPlayer = pMp;

                                        setMediaTextCallBack(sub);


                                        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

                                        if (video.getName().equals(Video.Name.WOODS)) {

                                            mediaPlayer.setLooping(true);

                                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                                            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, ADJUST_LOWER, 0);
                                            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, ADJUST_LOWER, 0);
                                            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, ADJUST_LOWER, 0);
                                            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, ADJUST_LOWER, 0);
                                            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, ADJUST_LOWER, 0);

                                        } else {

                                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                                            mediaPlayer.setLooping(false);
                                        }

                                        try {

                                            Gson gson = new Gson();
                                            MqttMessage m = new MqttMessage();
                                            video.setEvent("playbackStarted");
                                            m.setPayload(gson.toJson(video).getBytes());
                                            client.publish("ActorSystem/Halloween/Projector", m);

                                        } catch (MqttException e) {
                                            Log.e("MQTT", e.getMessage());
                                        }



                                    }
                                });

                                vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer pMp) {

                                        try {

                                            MqttMessage m = new MqttMessage();
                                            String mm = "{\"command\":\"songComplete\"}";
                                            m.setPayload(mm.getBytes());
                                            client.publish("ActorSystem/Halloween/Projector", m);

                                            Log.d("complete", "song finished!!!");

                                        } catch (MqttException e) {
                                            Log.e("MQTT", e.getMessage());
                                        }

                                    }
                                });


                                vv.start();




                            }
                        }, 200);
                    }
                });




            } else if (video.getCommand().equals("Pause")) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                vv.pause();
                            }
                        }, 200);

                    }

                });

            } else if (video.getCommand().equals("Resume")) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        vv.start();
                    }
                });


            } else {
                Log.d("dunno", "wtf command:"+video.getCommand());
            }

        } catch (Exception e) {
            Log.d(getClass().getCanonicalName(), e.getMessage());
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(getClass().getCanonicalName(), "Delivery complete");
    }

    @Override
    public void onTimedText(final MediaPlayer mp, final TimedText text) {
        if (text != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MqttMessage message = new MqttMessage();
                    message.setPayload(text.getText().getBytes());
                    try {
                        client.publish("ActorSystem/Halloween", message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    Log.d("timedText fired:", text.getText());

                }
            });
        }
    }

    public void setMediaTextCallBack(int sf){

        final ViewVideo thisVideoView = this;
        final int subFileName = sf;

        Handler h = new Handler(Looper.getMainLooper());
        h.post(new Runnable() {
            public void run() {

                try {

                    mediaPlayer.addTimedTextSource(getSubtitleFile(subFileName), MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);

                    int textTrackIndex = findTrackIndexFor(TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, mediaPlayer.getTrackInfo());
                    if (textTrackIndex >= 0) {
                        mediaPlayer.selectTrack(textTrackIndex);
                    } else {
                        Log.d("error", "Cannot find text track!");
                    }
                    mediaPlayer.setOnTimedTextListener(thisVideoView);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void createNote() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.PROGRESS_VISIBILITY_OFF);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private int findTrackIndexFor(int mediaTrackType, TrackInfo[] trackInfo) {
        int index = -1;
        for (int i = 0; i < trackInfo.length; i++) {
            if (trackInfo[i].getTrackType() == mediaTrackType) {
                return i;
            }
        }
        return index;
    }

    private String getSubtitleFile(int resId) {
        String fileName = getResources().getResourceEntryName(resId);
        File subtitleFile = getFileStreamPath(fileName);
        if (subtitleFile.exists()) {
            return subtitleFile.getAbsolutePath();
        }
        Log.d(TAG, "Subtitle does not exists, copy it from res/raw");

        // Copy the file from the res/raw folder to your app folder on the
        // device
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = getResources().openRawResource(resId);
            outputStream = new FileOutputStream(subtitleFile, false);
            copyFile(inputStream, outputStream);
            return subtitleFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStreams(inputStream, outputStream);
        }
        return "";
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int length = -1;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }

    // A handy method I use to close all the streams
    private void closeStreams(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable stream : closeables) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void playWoods() throws MqttException {
        MqttMessage m = new MqttMessage();
        String video = "{'command':'Play','name':'WOODS'}";
        m.setPayload(video.getBytes());
        client.publish("halloween/video", m);
    }

    private void resetMqttClient(){

        if (client != null){
            try {
                client.unsubscribe("halloween/video");
                client.close();
                client = null;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static ViewVideo getIntance() {
        return instace;
    }
}

