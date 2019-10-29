package com.videoedit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.videoedit.ringdroid.SongMetadataReader;
import com.videoedit.ringdroid.WaveformView;
import com.videoedit.ringdroid.soundfile.SoundFile;

import java.io.File;

/**
 * Created by dcang on 2019/10/28.
 */

public class RingdroidEditActivity extends AppCompatActivity implements WaveformView.WaveformListener{
    private WaveformView mWaveformView;
    private SoundFile mSoundFile;
    private float mDensity;
    private int mMaxPos;
    private int mStartPos;
    private int mEndPos;
    private String mFilename;
    private File mFile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringdroid);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;

        mFilename = getIntent().getStringExtra("path");
        initView();
    }

    private void initView(){
        mWaveformView = (WaveformView)findViewById(R.id.waveform);
        mWaveformView.setListener(this);

        mMaxPos = 0;
        if (mSoundFile != null && !mWaveformView.hasSoundFile()) {
            mWaveformView.setSoundFile(mSoundFile);
            mWaveformView.recomputeHeights(mDensity);
            mMaxPos = mWaveformView.maxPos();
        }
        loadFromFile();
    }

    private void loadFromFile() {
        mFile = new File(mFilename);

        SongMetadataReader metadataReader = new SongMetadataReader(
                this, mFilename);
    }


    private void finishOpeningSoundFile() {
        if(mSoundFile == null){
            return;
        }
        float d = mSoundFile.getDuration()/1000000.0f;
        float duration = Math.min(d,10);
        mWaveformView.setSoundFile(mSoundFile);
        mWaveformView.recomputeHeights(mDensity);

        mMaxPos = mWaveformView.maxPos();

        if (mEndPos > mMaxPos){
            mEndPos = mMaxPos;
        }


    }

    @Override
    public void waveformTouchStart(float x) {

    }

    @Override
    public void waveformTouchMove(float x) {

    }

    @Override
    public void waveformTouchEnd() {

    }

    @Override
    public void waveformFling(float x) {

    }

    @Override
    public void waveformDraw() {

    }

    @Override
    public void waveformZoomIn() {

    }

    @Override
    public void waveformZoomOut() {

    }

    @Override
    protected void onDestroy() {
        if(mSoundFile != null){
            mSoundFile.release();
            mSoundFile = null;

        }
        super.onDestroy();
    }

    public void onConfirm(View view){
        double start = Double.valueOf(formatTime(mStartPos));
        double end = Double.valueOf(formatTime(mEndPos));
        Intent intent = new Intent();
        intent.putExtra("startTime",start);
        intent.putExtra("endTime",end);
        intent.putExtra("path",mFilename);
        intent.putExtra("name",getIntent().getStringExtra("name"));
        intent.putExtra("channelCount",mSoundFile.getChannels());
        intent.putExtra("sampleRate",mSoundFile.getSampleRate());
        intent.putExtra("bitRate",mSoundFile.getAvgBitrateKbps());
        setResult(RESULT_OK,intent);
        finish();
    }




    private String formatTime(int pixels) {
        if (mWaveformView != null && mWaveformView.isInitialized()) {
            return formatDecimal(mWaveformView.pixelsToSeconds(pixels));
        } else {
            return "";
        }
    }

    private String formatDecimal(double x) {
        int xWhole = (int)x;
        int xFrac = (int)(100 * (x - xWhole) + 0.5);

        if (xFrac >= 100) {
            xWhole++; //Round up
            xFrac -= 100; //Now we need the remainder after the round up
            if (xFrac < 10) {
                xFrac *= 10; //we need a fraction that is 2 digits long
            }
        }

        if (xFrac < 10)
            return xWhole + ".0" + xFrac;
        else
            return xWhole + "." + xFrac;
    }
}
