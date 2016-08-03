package com.oneguy.libim;

import android.content.Context;

import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuoShu on 16/8/2.
 */
public class ImSdkAudio {
    private static final String TAG = "ImSdkAudio";
    private AudioPlayer audioPlayer;
    private List<AudioAttachment> pendingAudio;
    private Object audioMutex = new Object();

    public ImSdkAudio(Context context) {
        audioPlayer = new AudioPlayer(context);
        pendingAudio = new ArrayList<>();
        audioPlayer.setOnPlayListener(new ExtOnPlayListener());
    }

    public void requestPlay(AudioAttachment attachment) {
        synchronized (audioMutex) {
            if (audioPlayer.isPlaying()) {
                pendingAudio.add(attachment);
            } else {
                playAudio(attachment);
            }
        }
    }

    public void stop(){
        synchronized (audioMutex) {
            audioPlayer.stop();
            pendingAudio.clear();
        }
    }

    private void playAudio(AudioAttachment attachment) {
        audioPlayer.setDataSource(attachment.getUrl());
        audioPlayer.start(android.media.AudioManager.STREAM_MUSIC);
    }

    private void playNext() {
        synchronized (audioMutex) {
            if (pendingAudio.size() > 0) {
                AudioAttachment attachment = pendingAudio.remove(0);
                playAudio(attachment);
            }
        }
    }

    private class ExtOnPlayListener implements OnPlayListener {

        @Override
        public void onPrepared() {
        }

        @Override
        public void onCompletion() {
            playNext();
        }

        @Override
        public void onInterrupt() {
        }

        @Override
        public void onError(String error) {
            playNext();
        }

        @Override
        public void onPlaying(long curPosition) {
        }
    }
}
