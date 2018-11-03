package database;

import android.content.Context;

import com.groundmobile.ground.Constants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmConfig {
    public RealmConfiguration UserRealmVersion(Context context){

        Realm.init(context);    //realm 초기화
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("User.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                //.migration(new Migration())
                .build();

        return config;
    }

    public RealmConfiguration MemoRealmVersion(Context context){
        Realm.init(context);    //realm 초기화
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("Memo.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                //.migration(new Migration())
                .build();

        return config;
    }
}
