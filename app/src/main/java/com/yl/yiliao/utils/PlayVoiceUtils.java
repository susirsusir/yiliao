package com.yl.yiliao.utils;


import android.media.MediaPlayer;

import java.io.IOException;

public class PlayVoiceUtils {
    private static MediaPlayer mMediaPlayer;

    public static void playAudio(String url, MediaPlayer.OnCompletionListener onCompletionListener) {// 播放url音乐文件

        try {
            killMediaPlayer();// 播放前，先kill原来的mediaPlayer
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void killMediaPlayer() {
        // TODO Auto-generated method stub
        if (null != mMediaPlayer) {
            mMediaPlayer.release();
        }
    }
}
