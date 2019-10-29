package com.videoedit.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.videoedit.model.AudioModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcang on 2019/10/24.
 */

public class FindAudioFile {
    /**
     * 获取音频文件列表
     * @param context
     * @return
     */
    public List<AudioModel> getAudioFileList(Context context){
        List<AudioModel> list = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor != null){
            if(cursor.moveToFirst()){
                while (!cursor.isAfterLast()){
                    AudioModel audioModel = new AudioModel();
                    audioModel.name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    audioModel.filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    list.add(audioModel);
                    cursor.moveToNext();
                }
            }
        }
        return list;
    }
}
