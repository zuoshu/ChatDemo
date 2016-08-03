package oneguy.com.chatdemo;

import android.app.Application;

import com.oneguy.libim.ImSdk;

/**
 * Created by ZuoShu on 16/8/2.
 */
public class App extends Application {

    public void onCreate() {
        super.onCreate();
        ImSdk.initInApplication(this,Constants.NE_KEY);
    }
}