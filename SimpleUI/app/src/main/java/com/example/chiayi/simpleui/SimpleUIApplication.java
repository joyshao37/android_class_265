package com.example.chiayi.simpleui;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by chiayi on 16/5/5.
 */
public class SimpleUIApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("iEx8Nh5gvNl1Quf0Hy0PKleIBuxRrO01KtBFaEF4")
                        .clientKey("hd6WBbwqAidH59meE9sVs41KSZiN5xj6z6JZMbjn")
                        .server("https://parseapi.back4app.com/")
                        .build()
        );
    }
}
