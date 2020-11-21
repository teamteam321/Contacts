package com.akzaki.mycontacts.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.akzaki.mycontacts.model.User;
import com.akzaki.mycontacts.util.AppExecutors;
import com.akzaki.mycontacts.util.DateFormatter;

import java.util.Calendar;


@Database(entities = {User.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    private static final String DB_NAME = "user_1.db";

    private static AppDatabase sInstance;

    public abstract UserDao userDao();

    public static synchronized AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DB_NAME
            ).addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    insertInitialData(context);
                }
            }).build();
        }
        return sInstance;
    }

    private static void insertInitialData(final Context context) {
        AppExecutors exe = new AppExecutors();
        exe.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = getInstance(context);
                /*db.userDao().addUser(
                        new User(0,
                                "Natthawat",
                                "Khuwijitjaru",
                                //Calendar.getInstance().getTime(),
                                new DateFormatter().parseDateString("1999-04-26"),
                                phone, address, email, User.GENDER_MALE,
                                true),
                        new User(0,
                                "aaaaaaaaaa",
                                "bbbbbbbbbb",
                                Calendar.getInstance().getTime(),
                                phone, address, email, User.GENDER_MALE,
                                true)
                );*/
            }
        });



    }
}