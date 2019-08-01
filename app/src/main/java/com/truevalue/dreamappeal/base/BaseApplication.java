package com.truevalue.dreamappeal.base;

import android.app.Application;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
    }


    private void initRealm(){
//        Realm.init(this);
//
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .schemaVersion(1)
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        // Use the config
//        Realm.setDefaultConfiguration(config);
    }

}
