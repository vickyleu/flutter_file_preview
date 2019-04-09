package zzaning.com.filepreviewexample;

import zzaning.com.filepreview.TLog;
import zzaning.com.filepreview.ExceptionHandler;

import io.flutter.app.FlutterApplication;

public class ExampleApp extends FlutterApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        TLog.d("App Start");
        ExceptionHandler.getInstance().initConfig(this);
    }

}


