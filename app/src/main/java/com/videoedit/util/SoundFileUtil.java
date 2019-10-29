package com.videoedit.util;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import com.videoedit.model.AudioModel;
import com.videoedit.ringdroid.soundfile.SoundFile;

import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by dcang on 2019/10/28.
 */

public class SoundFileUtil {
    private int bitRate;
    private int mChannels;
    private int mSampleRate;

    public boolean init(AudioModel model){
        return getAudioInfo(model.filePath);
    }

    public boolean getAudioInfo(String fileName){
        File f = new File(fileName);
        if (!f.exists()) {
            return false;
        }
        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource(f.getPath());
//            MP3File  mp3File = new MP3File(fileName);
//            MP3AudioHeader header = mp3File.getMP3AudioHeader();
//            Log.e("wwww","bitRate ===="+ header.getBitRate());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

//        bitRate=====32
//
//        bitRate = 文件大小*1024*8/时长（秒）
//        bitRate = 文件大小*8/时长（秒）
//        比特率就是每秒多少B（位）
//        MediaFormat f1 = extractor.getTrackFormat(0);
//        int bitRate = f1.getInteger(MediaFormat.KEY_BIT_RATE);


        MediaFormat format = null;
        int numTracks = extractor.getTrackCount();
        for (int i=0; i<numTracks; i++) {
            format = extractor.getTrackFormat(i);
            if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                extractor.selectTrack(i);
                break;
            }
        }


        long duration = format.getLong(MediaFormat.KEY_DURATION);
        bitRate = (int) ((f.length()*1024*8)/duration);

//        int s = (int) (duration/1000000);
//        Log.e("wwww","秒 ===="+s);
//        Log.e("wwww","文件大小 ===="+(f.length()*1024*8));//文件大小 ====20592,19.69k,除5271552
//        Log.e("wwww","bitRate ===="+((f.length()*1024*8)/duration));//文件大小 ====20592,19.69k,除5271552

        mChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

//        Log.e("wwww","mChannels ===="+mChannels);
//        Log.e("wwww","mSampleRate ===="+mSampleRate);
        return true;
    }


    public int getChannels(){
        return mChannels;
    }

    public int getSampleRate(){
        return mSampleRate;
    }

    public int getAvgBitrateKbps(){
        return bitRate;
    }



}
