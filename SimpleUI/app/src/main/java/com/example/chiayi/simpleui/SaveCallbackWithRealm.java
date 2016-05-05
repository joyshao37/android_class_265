package com.example.chiayi.simpleui;

import com.parse.ParseException;
import com.parse.SaveCallback;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by chiayi on 16/5/5.
 */
public class SaveCallbackWithRealm implements SaveCallback {

    RealmObject realmObject;
    SaveCallback saveCallback;

    public  SaveCallbackWithRealm(RealmObject realmObject, SaveCallback callback){

        this.realmObject=realmObject;
        this.saveCallback=callback;
    }

    @Override
    public void done(ParseException e) {

        if(e==null) {
            Realm realm = Realm.getDefaultInstance();

            realm.beginTransaction();
            realm.copyToRealm(realmObject);
            realm.commitTransaction();

            realm.close();

        }
        saveCallback.done(e);


    }
}
