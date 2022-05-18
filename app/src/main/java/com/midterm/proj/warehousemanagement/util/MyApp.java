package com.midterm.proj.warehousemanagement.util;
import android.app.Application;
import android.content.Context;

public class MyApp extends Application{
    public static Context context = null;
    //public static CrudAllEventListener crudAllEventListener;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
