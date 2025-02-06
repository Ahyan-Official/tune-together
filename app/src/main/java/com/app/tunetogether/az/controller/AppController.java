package com.app.tunetogether.az.controller;

import android.app.Application;


public class AppController extends Application {

    private static AppController instance;

    //Controller class
    public static synchronized AppController getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }
}
