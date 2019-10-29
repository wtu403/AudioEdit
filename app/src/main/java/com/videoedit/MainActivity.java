package com.videoedit;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.videoedit.audio.SimpleLame;
import com.videoedit.mediacodec.AudioEncoder;
import com.videoedit.model.AudioHolder;
import com.videoedit.model.AudioModel;
import com.videoedit.util.FindAudioFile;
import com.videoedit.util.SoundFileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AudioEncoder audioEncoder = new AudioEncoder();
    public String filePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(SimpleLame.stringFromJNI());

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirm();
            }
        });

    }

    private void nextActivity() {
        List<AudioModel> list = new FindAudioFile().getAudioFileList(this);
        filePath = list.get(0).filePath;
        initAudio(list);
    }



    private void initAudio(List<AudioModel> list){
        SoundFileUtil util = new SoundFileUtil();

        for (AudioModel model : list){
            AudioHolder audioHolder = new AudioHolder();
            audioHolder.file = model.filePath;
            audioHolder.name = model.name ;
            if (!util.init(model)){
                break;
            }
            audioHolder.bitRate = util.getAvgBitrateKbps();
            audioHolder.sampleRate = util.getSampleRate();
            audioHolder.channelCount = util.getChannels();
            audioHolder.start = 0;
            audioHolder.end = 5;//TODO 不可超过长度
            audioList.add(audioHolder);
        }

        Log.e("wwww","完成");
    }

    private List<AudioHolder> audioList = new ArrayList<>();
    public void onConfirm(){
        if(audioList.size() == 0){
            return;
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/HMSDK";
        File f = new File(path);
        if (!f.exists()){
            f.mkdir();
        }
        audioEncoder.start(path+"/test_merge.mp3",audioList);
    }
}
