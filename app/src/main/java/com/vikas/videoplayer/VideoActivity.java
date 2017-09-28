package com.vikas.videoplayer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Vikas on 9/28/2017.
 */

public class VideoActivity extends AppCompatActivity {
    public static String TAG="VideoActivity";
    String video_file;
    VideoView video_view;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        Intent intent=getIntent();
        video_file=intent.getStringExtra("VIDEO_FILE");
        Log.e(TAG, "video_file "+video_file);
        init();
        playVideo();

    }

    private void playVideo() {
        pDialog.show();
        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoActivity.this);
            mediacontroller.setAnchorView(video_view);
            // Get the URL from String VideoURL
            Uri video = Uri.parse("http://genorainnovations.com/Audio/media/video/"+video_file);
            video_view.setMediaController(mediacontroller);
            video_view.setVideoURI(video);

        } catch (Exception e) {
            Log.e(TAG, "Exception"+e.getMessage());
            e.printStackTrace();
        }

        video_view.requestFocus();
        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                video_view.start();
            }
        });

    }

    private void init() {
        video_view= (VideoView) findViewById(R.id.video_view);
        // Create a progressbar
        pDialog = new ProgressDialog(VideoActivity.this);
        // Set progressbar title
        pDialog.setTitle(" Video Streaming ");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

    }
}
