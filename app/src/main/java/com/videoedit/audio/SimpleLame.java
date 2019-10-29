package com.videoedit.audio;

import com.videoedit.mediacodec.AudioEncoder;

/**
 * Created by dcang on 2019/10/23.
 */

public class SimpleLame {
    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();

    /**
     * pcm文件转换mp3函数
     */
    public static native void convert(AudioEncoder encoder, String jwav, String jmp3,
                                      int inSampleRate, int outChannel, int outSampleRate, int outBitrate,
                                      int quality);
}
