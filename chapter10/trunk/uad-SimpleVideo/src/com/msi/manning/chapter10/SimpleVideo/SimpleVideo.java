package com.msi.manning.chapter10.SimpleVideo;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class SimpleVideo extends Activity {

    private VideoView myVideo;
    private MediaController mc;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.main);
        
        this.myVideo = (VideoView) findViewById(R.id.video);
        this.myVideo.setVideoPath("sdcard/test.mp4");
        this.mc = new MediaController(this);
        this.mc.setMediaPlayer(this.myVideo);
        this.myVideo.setMediaController(this.mc);
        this.myVideo.requestFocus();
    }
}
