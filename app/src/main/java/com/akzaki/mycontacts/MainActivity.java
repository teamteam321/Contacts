package com.akzaki.mycontacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.akzaki.mycontacts.adapter.UserAdapter;
import com.akzaki.mycontacts.db.AppDatabase;
import com.akzaki.mycontacts.model.User;
import com.akzaki.mycontacts.util.AppExecutors;
import com.akzaki.mycontacts.util.DateFormatter;
import com.akzaki.mycontacts.util.UserManage;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GGG";
    private LinearLayoutManager llm;
    private Parcelable lstate;
    //private static final String TAG = MainActivity.class.getName();
    private RecyclerView mreCyclerView;
    @Override
    protected  void onResume() {
        super.onResume();
        updateRecyclerView();
        llm.onRestoreInstanceState(lstate);

    }
    protected void onPause() {
        super.onPause();
        lstate = llm.onSaveInstanceState();
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mreCyclerView = findViewById(R.id.userRecyclerView);
        llm = new LinearLayoutManager(MainActivity.this);
        mreCyclerView.setLayoutManager(llm);
        ///update recyclerciew

        this.updateRecyclerView();


        ////

        Button add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,AddUser.class);
                i.putExtra("MODE","ADD");
                startActivity(i);
            }
        });
        Button searchB = findViewById(R.id.gghh);
        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SearchActivity.class);
                //i.putExtra("MODE","ADD");
                startActivity(i);
            }
        });

    }



    private void updateRecyclerView() {
        final AppExecutors exe = new AppExecutors();
        exe.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(MainActivity.this);
                User[] outt = db.userDao().getAllUser();
                final User[] out = UserManage.sortUser(outt);
                exe.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        //on main thread
                        UserAdapter adapter = new UserAdapter(MainActivity.this, out);
                        mreCyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }


}