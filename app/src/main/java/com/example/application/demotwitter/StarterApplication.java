package com.example.application.demotwitter;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class StarterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("3364eb8b821f289b9793904759a29e38292f8721")
                .clientKey("e1f9452542be0c68c592674677c0fcd200a4946b")
                .server("http://52.14.192.20:80/parse/")
                .build()
        );
//    iOceW1fjiDF1

//Log.i("oncreate ", "adsf");
//        ParseObject parseObject = new ParseObject("Example");
//        parseObject.put("username", "test");
//        parseObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e== null) {
//                    Log.i("success", "success");
//                } else {
//                    Log.i("error", e.getMessage());
//                }
//            }
//        });

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}