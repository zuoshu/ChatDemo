package oneguy.com.chatdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.oneguy.libim.ImSdkAudio;
import com.oneguy.libim.ImSdk;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    @Bind(R.id.account_spinner)
    Spinner accountSpinner;

    @Bind(R.id.room_spinner)
    Spinner roomSpinner;

    @Bind(R.id.login)
    Button login;

    @Bind(R.id.logout)
    Button logout;

    @Bind(R.id.room_layout)
    View roomLayout;

    @Bind(R.id.enter_room)
    Button enterRoom;

    @Bind(R.id.exit_room)
    Button exitRoom;

    @Bind(R.id.info)
    TextView info;

    @Bind(R.id.input)
    EditText input;

    @Bind(R.id.speek)
    Button speek;

    List<Account> accountList;
    List<Integer> roomList;
    String[] accountItems;
    String[] roomItems;

    ImSdk imSdk;
    ImSdkAudio imSdkAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initAccount();
        initRoom();
        initAdapter();
        requestBasicPermission();
        imSdk = new ImSdk(this);
        imSdkAudio = new ImSdkAudio(this);
        imSdk.setChatRoomMessageCallback(new ImSdk.ChatRoomMessageCallback() {
            @Override
            public void onMessage(ChatRoomMessage message) {
                log("FROM:" + message.getFromAccount() + " RECEIVE:"
                        + message.getMsgType() + " " + message.getContent());
                if (message.getMsgType().equals(MsgTypeEnum.audio)) {
                    imSdkAudio.requestPlay((AudioAttachment) message.getAttachment());
                }
            }
        });
        imSdk.setSendAudioMessageCallback(new ExtSendAudioMessageCallback());

        speek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String roomId = String.valueOf(roomList.get(roomSpinner.getSelectedItemPosition()));
                    imSdk.startRecord(roomId);
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL
                        || event.getAction() == MotionEvent.ACTION_UP) {
                    imSdk.stopRecord();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (isCancelled(v, event)) {
                        imSdk.cancelRecord();
                    }
                }
                return false;
            }
        });
    }

    private void requestBasicPermission() {
        MPermission.with(MainActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

    private void initAccount() {
        accountList = new ArrayList<>();
        accountList.add(new Account("tthp0", "ba2d3d10e0cacc4cf2faa4a868dea855"));
        accountList.add(new Account("tthp1", "8de347e1d9a75499457fce10d7c071c9"));
        accountList.add(new Account("tthp2", "75134fc7d6db66d2fc823aa0fd4047c4"));
        accountList.add(new Account("tthp3", "b6e13429ed09be648708c9664b6a7c0a"));
        accountList.add(new Account("tthp4", "5f47abd57387c21c90c872168b49e7f2"));
        accountList.add(new Account("tthp5", "d62c3a4c03d8bfb9faef56c7b524a3ae"));
        accountList.add(new Account("tthp6", "8588b15535996efd3c5ebdb9c5432505"));
        accountList.add(new Account("tthp7", "8c75aa2a2ef5d4b05ae2652729cae950"));
        accountList.add(new Account("tthp8", "f8f5d8b2ec5061906714045cc77c9d78"));
        accountList.add(new Account("tthp9", "1d7a145ffe69aebb05b4d11c4c7239b6"));
    }

    private void initRoom() {
        roomList = new ArrayList<>();
        roomList.add(3763485);
        roomList.add(3762553);
        roomList.add(3762554);
        roomList.add(3762555);
        roomList.add(3762556);
        roomList.add(3761460);
        roomList.add(3761461);
        roomList.add(3763486);
        roomList.add(3763487);
        roomList.add(3762557);
        roomList.add(3758444);
        roomList.add(3761462);
        roomList.add(3760461);
        roomList.add(3763488);
        roomList.add(3763489);
        roomList.add(3759241);
        roomList.add(3762558);
        roomList.add(3763490);
    }

    private void initAdapter() {
        String[] mItems = getAccountItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(adapter);

        String[] roomItems = getRoomItems();
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomItems);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(roomAdapter);
    }

    private String[] getAccountItems() {
        String[] items = new String[accountList.size()];
        for (int i = 0; i < accountList.size(); i++) {
            items[i] = accountList.get(i).getAccid();
        }
        return items;
    }

    private String[] getRoomItems() {
        String[] items = new String[roomList.size()];
        for (int i = 0; i < roomList.size(); i++) {
            items[i] = String.valueOf(roomList.get(i));
        }
        return items;
    }

    @OnClick(R.id.login)
    public void onLoginClick(View view) {
        final Account account = accountList.get(accountSpinner.getSelectedItemPosition());
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        LogHelper.warn(TAG, "login success");
                        log("login success with:" + account.getAccid());
                        logout.setVisibility(View.VISIBLE);
                        login.setVisibility(View.GONE);
                        accountSpinner.setEnabled(false);
                        roomLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailed(int code) {
                        LogHelper.warn(TAG, "login fail:" + code);
                        log("login fail with:" + account.getAccid() + " code:" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        LogHelper.warn(TAG, "login onException");
                        log("login fail with exception");
                    }
                };
        imSdk.login(account.getAccid(), account.getToken(), callback);
    }

    @OnClick(R.id.logout)
    public void onLogoutClick(View view) {
        imSdk.logout();
        logout.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
        accountSpinner.setEnabled(true);
        roomLayout.setVisibility(View.INVISIBLE);
        LogHelper.warn(TAG, "logout");
        log("login out");
    }

    @OnClick(R.id.enter_room)
    public void onEnterRoomClick(View v) {
        final int roomId = roomList.get(roomSpinner.getSelectedItemPosition());
        EnterChatRoomData data = new EnterChatRoomData(String.valueOf(roomId));
        RequestCallback<EnterChatRoomResultData> callback = new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData param) {
                LogHelper.warn(TAG, "enter room success");
                log("enter room success:" + roomId);
                enterRoom.setVisibility(View.GONE);
                exitRoom.setVisibility(View.VISIBLE);
                roomSpinner.setEnabled(false);
            }

            @Override
            public void onFailed(int code) {
                LogHelper.warn(TAG, "enter room fail:" + code);
                log("enter room:" + roomId + " fail code:" + code);
            }

            @Override
            public void onException(Throwable exception) {
                LogHelper.warn(TAG, "enter room onException");
                log("enter room exception");
            }
        };
        imSdk.enterRoom(String.valueOf(roomId), callback);
    }

    @OnClick(R.id.exit_room)
    public void onExitRoomClick(View v) {
        int roomId = roomList.get(roomSpinner.getSelectedItemPosition());
        imSdk.exitRoom(String.valueOf(roomId));
        enterRoom.setVisibility(View.VISIBLE);
        exitRoom.setVisibility(View.GONE);
        roomSpinner.setEnabled(true);
        log("exit room success:");
    }

    @OnClick(R.id.send)
    public void onSendClick(View v) {
        String text = input.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }

        int roomId = roomList.get(roomSpinner.getSelectedItemPosition());

        imSdk.sendChatRoomTextMessage(String.valueOf(roomId), text);
        input.setText("");
        log("ROOM ID:" + roomId + " TEXT MESSAGE:" + text);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (imSdk != null) {
            imSdk.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imSdk != null) {
            imSdk.onResume();
        }
    }

    private void log(String log) {
        info.append(log + "\n");
    }

    private class ExtSendAudioMessageCallback implements ImSdk.SendAudioMessageCallback {

        @Override
        public void onSuccess() {
            log("SEND AUDIO SUCCESS");
        }

        @Override
        public void onFail() {
            log("SEND AUDIO FAIL");
        }

        @Override
        public void onCancel() {
            log("SEND AUDIO CANCEL");
        }
    }

    // 上滑取消录音判断
    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40) {
            return true;
        }

        return false;
    }
}
