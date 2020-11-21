package com.akzaki.mycontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akzaki.mycontacts.db.AppDatabase;
import com.akzaki.mycontacts.model.User;
import com.akzaki.mycontacts.util.AppExecutors;
import com.akzaki.mycontacts.util.DateFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Date;

import com.akzaki.mycontacts.util.*;

public class AddUser extends AppCompatActivity {

    //private static final String TAG = "addf";
    private Calendar mCalendar;
    private EditText dateEdit;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Intent i = getIntent();
        final String MODE = i.getStringExtra("MODE");


        TextView st = findViewById(R.id.add_update_text);

        mCalendar =  Calendar.getInstance();

        dateEdit = findViewById(R.id.date_picker);
        final Button confirm = findViewById(R.id.confirm_button);
        Button cancel = findViewById(R.id.cancel_button);
        final RadioGroup rg = findViewById(R.id.gender_radio_group);
        final RadioButton mb = findViewById(R.id.male_radio);
        final RadioButton fb = findViewById(R.id.female_radio);
        final EditText first_name = ((EditText)findViewById(R.id.first_name_input));
        final EditText last_name = ((EditText)findViewById(R.id.last_name_input));

        final EditText phone = ((EditText)findViewById(R.id.phone_input));
        final EditText email = ((EditText)findViewById(R.id.email_input));
        final EditText address = ((EditText)findViewById(R.id.address_input));
        final CheckBox single_check = ((CheckBox)findViewById(R.id.single_check));
        int UID = 0;

        if(MODE.equalsIgnoreCase("EDIT")){

            st.setText("Edit Infomation");
            String user_string = i.getStringExtra("USER");

            //final String final_user
            User us = new GsonBuilder().serializeNulls().create().fromJson(user_string,User.class);

            UID = us.id;
            //Log.i(TAG,"passed_string: "+user_string);

            if(us.firstName!=null) first_name.setText(us.firstName);
            if(us.lastName.length()>0) last_name.setText(us.lastName);
            if(us.phone.length()>0) phone.setText(us.phone);
            if(us.email.length()>0) email.setText(us.email);
            if(us.address.length()>0) address.setText(us.address);
            if(us.gender == User.GENDER_MALE){
                rg.check(R.id.male_radio);
            }else{
                rg.check(R.id.female_radio);
            }
            if(us.single)
                single_check.setChecked(true);

            mCalendar.setTime(us.birthDate);
            dateEdit.setText(DateFormatter.formatForUi(us.birthDate));


        }


        dateEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpa = new DatePickerDialog(
                        AddUser.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                mCalendar.set(Calendar.YEAR, year);
                                mCalendar.set(Calendar.MONTH, month);
                                mCalendar.set(Calendar.DAY_OF_MONTH, day);
                                dateEdit.setText(DateFormatter.formatForUi(mCalendar.getTime()));
                            }
                        },
                        mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
                );
                dpa.show();
            }
        });

        final int FUID = UID;

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gather infomation

                String temp_fname = first_name.getText().toString();
                String temp_lname = last_name.getText().toString();
                String temp_email = email.getText().toString().trim();
                String temp_phone = phone.getText().toString().trim();

                Date birth = mCalendar.getTime();
                int selected_gender = (rg.getCheckedRadioButtonId() == R.id.male_radio)? User.GENDER_MALE : User.GENDER_FEMALE;
                boolean single = single_check.isChecked();

                //sanity check
                if(temp_fname.trim().length() == 0 || temp_lname.trim().length() == 0){
                    Toast ty = Toast.makeText(AddUser.this,"Please Enter Name!",Toast.LENGTH_LONG);
                    ty.show();
                    return;
                }

                if(temp_email.indexOf('@') < 0
                        || temp_email.indexOf('.') < 0 ||
                        (temp_email.indexOf('@') != temp_email.lastIndexOf('@'))){
                    Toast ty = Toast.makeText(AddUser.this,"Invalid Email!",Toast.LENGTH_LONG);
                    ty.show();
                    return;
                }

                if(temp_phone.length() != 10 && temp_phone.indexOf("+")!=0){
                    Toast ty = Toast.makeText(AddUser.this,"Invalid Phone Number!",Toast.LENGTH_LONG);
                    ty.show();
                    return;
                }



                final User newUser = new User(FUID,
                        temp_fname,
                        temp_lname,
                        //Calendar.getInstance().getTime(),
                        birth,
                        temp_phone,
                        address.getText().toString().trim(),  //can't do much
                        temp_email,
                        selected_gender,
                        single);

                //Log.i("addf",(selected_gender == User.GENDER_MALE)?"male":"female");

                AppExecutors exe = new AppExecutors();
                exe.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        AppDatabase db = AppDatabase.getInstance(AddUser.this);

                        if(MODE.equalsIgnoreCase("EDIT")){
                            db.userDao().updateUser(newUser);
                        }
                        else{
                            db.userDao().addUser(newUser);
                        }


                        finish();
                    }
                });
                Toast ty = Toast.makeText(AddUser.this,(MODE.equalsIgnoreCase("EDIT"))?"Updated!":"Added!",Toast.LENGTH_LONG);
                ty.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}