package com.gmail.a93ak.andrei19.finance30;

import android.app.Application;
import android.os.Environment;

import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;


public class App extends Application {

    private static String imagePath;
    private static String tempImagePath;

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.getInstance().setContext(this);
        imagePath = getApplicationInfo().dataDir + "/files/images/";
        tempImagePath = Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
    }

    public static String getImagePath(long id) {
        return imagePath + String.valueOf(id) + ".jpg";
    }

    public static String getTempImagePath() {
        return tempImagePath;
    }
}
