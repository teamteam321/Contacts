package com.akzaki.mycontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akzaki.mycontacts.adapter.UserAdapter;
import com.akzaki.mycontacts.db.AppDatabase;
import com.akzaki.mycontacts.model.User;
import com.akzaki.mycontacts.util.AppExecutors;

import java.util.ArrayList;

import com.akzaki.mycontacts.util.UserManage;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView sreCyclerView;
    private static final String TAG = "SearchActivity";

    EditText input;
    @Override
    protected void onResume() {
        super.onResume();
        updateSearchRecyclerView(input.getText().toString());
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sreCyclerView = findViewById(R.id.result_recycler_view);
        sreCyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        input = ((EditText)findViewById(R.id.search_input));

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateSearchRecyclerView(input.getText().toString());
                Log.i("addf",input.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }


    private void updateSearchRecyclerView(String ttt) {

        final String[] text_arr = (ttt.toLowerCase()).split(" ");

        final AppExecutors exe = new AppExecutors();
        exe.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(SearchActivity.this);
                final User[] out = db.userDao().getAllUser();
                ArrayList <User> result = new ArrayList<User>();
                Log.i("addf","--------");
                //incase split keyword
                for(int r = 0;r<text_arr.length;r++){
                    String text = text_arr[r];

                    for (User f : out) {
                        Log.i("addf",text+" "+f.firstName.toLowerCase()+" "+f.firstName.toLowerCase().indexOf(text));
                        if(     f.firstName.toLowerCase().indexOf(text) >= 0 ||
                                f.lastName.toLowerCase().indexOf(text) >= 0||
                                f.email.indexOf(text) >= 0||
                                f.address.indexOf(text) >= 0||
                                f.phone.indexOf(text) >= 0
                        ) if(!result.contains(f)) // possible multiple matches
                            result.add(f);
                    }

                }
                User[] res = new User[result.size()];
                for(int e = 0;e<result.size();e++){
                    res[e] = result.get(e);
                }
                final User[]res_f = UserManage.sortUser(res);

                exe.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        UserAdapter adapter = new UserAdapter(SearchActivity.this, res_f);
                        sreCyclerView.setAdapter(adapter);
                        Toast t = Toast.makeText(SearchActivity.this,"Found(s) "+res_f.length,Toast.LENGTH_SHORT);
                        t.show();
                    }
                });


            }
        });

    }
}