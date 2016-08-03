package com.oneguy.libim;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;

import java.io.File;
import java.util.List;

/**
 * Created by ZuoShu on 16/8/3.
 */
public class ImSdk {
    private RequestCallback<LoginInfo> mLoginCallback;
    protected AudioRecorder mAudioRecorder;
    private boolean mStarted = false;
    private boolean mCancelled = false;
    private boolean mTouched = false;

    private String mRoomId;
    private Activity mActivity;
    private SendAudioMessageCallback mAudioMessageCallback;
    private ChatRoomMessageCallback mChatRoomMessageCallback;
    private Observer<List<ChatRoomMessage>> messageObserver;

    private volatile boolean mIsListening;
    private Object listenMutex = new Object();

    public static void initInApplication(Context context, String appKey) {
        SDKOptions options = new SDKOptions();

        options.appKey = appKey;

        NIMClient.init(context, null, options);

    }


    public interface SendAudioMessageCallback {
        void onSuccess();

        void onFail();

        void onCancel();
    }

    public interface ChatRoomMessageCallback {
        void onMessage(ChatRoomMessage message);
    }

    public ImSdk(Activity activity) {
        mActivity = activity;
        messageObserver = new Observer<List<ChatRoomMessage>>() {
            @Override
            public void onEvent(List<ChatRoomMessage> messages) {
                for (ChatRoomMessage message : messages) {
                    onIncomingMessage(message);
                }
            }
        };
        listenerIncomingMessage();
    }

    private void listenerIncomingMessage() {
        synchronized (listenMutex) {
            if (!mIsListening) {
                NIMClient.getService(ChatRoomServiceObserver.class)
                        .observeReceiveMessage(messageObserver, true);
            }
            mIsListening = true;
        }
    }

    private void unListenIncomingMessage() {
        synchronized (listenMutex) {
            if (mIsListening) {
                NIMClient.getService(ChatRoomServiceObserver.class)
                        .observeReceiveMessage(messageObserver, false);
            }
            mIsListening = false;
        }
    }

    private void onIncomingMessage(ChatRoomMessage message) {
        if (mChatRoomMessageCallback != null) {
            mChatRoomMessageCallback.onMessage(message);
        }
    }

    public void setLoginCallback(RequestCallback<LoginInfo> callback) {
        this.mLoginCallback = callback;
    }

    public void setChatRoomMessageCallback(ChatRoomMessageCallback callback) {
        mChatRoomMessageCallback = callback;
    }

    public void login(String accid, String token, RequestCallback<LoginInfo> callback) {
        LoginInfo info = new LoginInfo(accid, token);
        if (callback != null) {
            NIMClient.getService(AuthService.class)
                    .login(info)
                    .setCallback(callback);
        } else {
            NIMClient.getService(AuthService.class)
                    .login(info);
        }
    }

    public void login(String accid, String token) {
        login(accid, token, null);
    }

    public void logout() {
        NIMClient.getService(AuthService.class)
                .logout();
    }

    public void enterRoom(String roomId, RequestCallback<EnterChatRoomResultData> callback) {
        EnterChatRoomData data = new EnterChatRoomData(String.valueOf(roomId));
        if (callback != null) {
            NIMClient.getService(ChatRoomService.class)
                    .enterChatRoom(data)
                    .setCallback(callback);
        } else {
            NIMClient.getService(ChatRoomService.class)
                    .enterChatRoom(data);
        }
    }

    public void enterRoom(String roomId) {
        enterRoom(roomId, null);
    }

    public void exitRoom(String roomId) {
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
    }

    public void setSendAudioMessageCallback(SendAudioMessageCallback callback) {
        mAudioMessageCallback = callback;
    }

    public void startRecord(String roomId) {
        this.mRoomId = roomId;
        mTouched = true;
        initAudioRecord();
        onStartAudioRecord();
    }

    public void stopRecord() {
        mTouched = false;
        onEndAudioRecord(false);
    }

    public void cancelRecord() {
        mTouched = false;
        cancelAudioRecord();
    }

    public void onResume() {
        listenerIncomingMessage();
    }

    public void onPause() {
        if (mAudioRecorder != null) {
            onEndAudioRecord(true);
        }
        unListenIncomingMessage();
    }

    public void sendChatRoomTextMessage(String roomId, String content, RequestCallback<Void> callback) {
        if (TextUtils.isEmpty(content)) {
            return;
        }

        ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(
                String.valueOf(roomId),
                content
        );

        if (callback != null) {
            NIMClient.getService(ChatRoomService.class)
                    .sendMessage(message, true)
                    .setCallback(callback);
        } else {
            NIMClient.getService(ChatRoomService.class)
                    .sendMessage(message, true);
        }
    }

    public void sendChatRoomTextMessage(String roomId, String content) {
        sendChatRoomTextMessage(roomId, content, null);
    }

    private void cancelAudioRecord() {
        // reject
        if (!mStarted) {
            return;
        }
        // no change
        if (mCancelled) {
            return;
        }
        mCancelled = true;
    }

    private void onEndAudioRecord(boolean cancel) {
        mActivity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mAudioRecorder.completeRecord(cancel);
    }

    private void initAudioRecord() {
        if (mAudioRecorder == null) {
            mAudioRecorder = new AudioRecorder(mActivity, RecordType.AAC
                    , AudioRecorder.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND
                    , new ExtAudioCallback());
        }
    }

    private void onStartAudioRecord() {
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mStarted = mAudioRecorder.startRecord();
        mCancelled = false;
        if (mStarted == false) {
            if (mAudioMessageCallback != null) {
                mAudioMessageCallback.onFail();
            }
            return;
        }

        if (!mTouched) {
            return;
        }
    }

    private class ExtAudioCallback implements IAudioRecordCallback {

        @Override
        public void onRecordReady() {
        }

        @Override
        public void onRecordStart(File audioFile, RecordType recordType) {
        }

        @Override
        public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
            ChatRoomMessage message = ChatRoomMessageBuilder
                    .createChatRoomAudioMessage(mRoomId, audioFile, audioLength);
            NIMClient.getService(ChatRoomService.class).sendMessage(message, true);
            if (mAudioMessageCallback != null) {
                mAudioMessageCallback.onSuccess();
            }
        }

        @Override
        public void onRecordFail() {
            if (mAudioMessageCallback != null) {
                mAudioMessageCallback.onFail();
            }
        }

        @Override
        public void onRecordCancel() {
            if (mAudioMessageCallback != null) {
                mAudioMessageCallback.onCancel();
            }
        }

        @Override
        public void onRecordReachedMaxTime(int maxTime) {
            mAudioRecorder.handleEndRecord(true, maxTime);
        }
    }
}
