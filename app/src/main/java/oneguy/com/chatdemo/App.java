package oneguy.com.chatdemo;

import android.app.Application;

import com.netease.nimlib.sdk.NIMClient;

/**
 * Created by ZuoShu on 16/8/2.
 */
public class App extends Application {

    public void onCreate() {
        super.onCreate();
        NIMClient.init(this, null, null);
    }
}