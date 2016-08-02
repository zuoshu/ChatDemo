package oneguy.com.chatdemo;

import android.content.Context;

import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuoShu on 16/8/2.
 */
public class AudioManager {
    private static final String TAG = "AudioManager";
    private static AudioManager mInstance;
    private AudioPlayer audioPlayer;
    private List<AudioAttachment> pendingAudio;
    private Object audioMutex = new Object();

    public synchronized static AudioManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AudioManager(context);
        }
        return mInstance;
    }

    public AudioManager(Context context) {
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

    private void playAudio(AudioAttachment attachment) {
        audioPlayer.setDataSource(attachment.getUrl());
        audioPlayer.start(android.media.AudioManager.STREAM_MUSIC);
    }

    private class ExtOnPlayListener implements OnPlayListener {

        @Override
        public void onPrepared() {
            LogHelper.warn(TAG, "onPrepared");
        }

        @Override
        public void onCompletion() {
            LogHelper.warn(TAG, "onCompletion");
        }

        @Override
        public void onInterrupt() {
            LogHelper.warn(TAG, "onInterrupt");
        }

        @Override
        public void onError(String error) {
            LogHelper.warn(TAG, "onError:" + error);
        }

        @Override
        public void onPlaying(long curPosition) {
            LogHelper.warn(TAG, "onPlaying:" + curPosition);
        }
    }
}
