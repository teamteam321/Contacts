package com.akzaki.mycontacts.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.akzaki.mycontacts.AddUser;
import com.akzaki.mycontacts.BlankActivity;
import com.akzaki.mycontacts.MainActivity;
import com.akzaki.mycontacts.R;
import com.akzaki.mycontacts.db.AppDatabase;
import com.akzaki.mycontacts.model.User;
import com.akzaki.mycontacts.util.AppExecutors;
import com.akzaki.mycontacts.util.DateFormatter;
import com.google.gson.Gson;

import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    //private static final String TAG = "addf";
    private Context context;
    private User[] userList;
    public UserAdapter(Context cont, User[] u) {
        this.userList = u;
        this.context = cont;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return userList.length;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.id = userList[position].id;
        final Integer temp = userList[position].id;

        holder.fullName.setText(userList[position].firstName+" "+userList[position].lastName);
        //holder.birthDate.setText(DateFormatter.formatForUi(userList[position].birthDate));

        holder.firstName = userList[position].firstName;
        holder.lastName = userList[position].lastName;

        if(userList[position].email.length() > 0)
            holder.email.setText(userList[position].email);
        String temp_phone = userList[position].phone;

        if(temp_phone.length() >= 10)
        holder.phone.setText(temp_phone.substring(0,3)+"-"+temp_phone.substring(3,6)+"-"+temp_phone.substring(6));

        holder.single = userList[position].single;
        holder.single_c.setBackgroundColor((userList[position].single ? Color.parseColor("#00BFFF") : Color.RED) );
        if(userList[position].gender == User.GENDER_FEMALE)
            holder.genderImage.setImageResource(R.drawable.female_avatar);
        else
            holder.genderImage.setImageResource(R.drawable.male_avatar);

        holder.gender = userList[position].gender;
        //set other resource for windows

        holder.birthDate = userList[position].birthDate;
        holder.address = userList[position].address;

        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu pp = new PopupMenu(view.getContext(), view);
                pp.inflate(R.menu.popup_menu);
                pp.show();

                return false;
            }
        });*/


    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener{
        Integer id = -1;
        TextView fullName; //first + last name

        String firstName;
        String lastName;

        Date birthDate;
        ImageView genderImage;
        int gender;
        TextView single_c;
        boolean single; //true for single;
        TextView phone;
        TextView email;
        String address;
        //Button edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullname_textview);
            //birthDate = itemView.findViewById(R.id.birthdate_textview);
            email = itemView.findViewById(R.id.email_textview);
            //address = itemView.findViewById(R.id.address_input);

            phone = itemView.findViewById(R.id.phone_textview);

            genderImage = itemView.findViewById(R.id.imageview);
            single_c = itemView.findViewById(R.id.single_textview);
            //edit = itemView.findViewById(R.id.edit_button);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        final User[] temp = new User[1];
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.edit_menu: {


                    final AppExecutors exe = new AppExecutors();
                    exe.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase db = AppDatabase.getInstance(context);
                            temp[0] = db.userDao().getUserById(id);
                            //Log.i(TAG,temp[0].toString());
                            Intent i = new Intent(context,AddUser.class);
                            i.putExtra("MODE","EDIT");
                            i.putExtra("USER", new Gson().toJson(temp[0]));
                            //i.putExtra("uuu","testtest");
                            context.startActivity(i);
                        }
                    });

                    break;
                }
                case R.id.delete_menu: {
                    AlertDialog.Builder dial = new AlertDialog.Builder(context);
                    dial.setTitle("Confirmation!");
                    dial.setMessage("Do you want to Delete \""+fullName.getText().toString()+"\"?");
                    dial.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final AppExecutors exe = new AppExecutors();
                            exe.diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    AppDatabase db = AppDatabase.getInstance(context);
                                    Log.i("IDDD",id+"");
                                    db.userDao().deleteUser(db.userDao().getUserById(id));

                                }
                            });
                            Toast t = Toast.makeText(context,"Deleted!",Toast.LENGTH_SHORT);
                            t.show();
                            Intent x = new Intent(context, BlankActivity.class);
                            context.startActivity(x);

                        }
                    });
                    dial.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dial.show();

                    break;
                }

            }
            return false;
        }

        @Override
        public boolean onLongClick(View view) {
            PopupMenu pp = new PopupMenu(view.getContext(), view);
            pp.inflate(R.menu.popup_menu);
            pp.setOnMenuItemClickListener(this);
            pp.show();
            return false;
        }

        @Override
        public void onClick(View view) {
            //LayoutInflater inflater = LayoutInflater.from(view.getContext());
            //ViewGroup popupView = (ViewGroup)inflater.inflate(R.layout.popup_window,null);
            LayoutInflater li = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);



            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            PopupWindow pw = new PopupWindow(li.inflate(R.layout.popup_window,
                    null, false), width, height, true);

            //((MainActivity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            TextView fw = pw.getContentView().findViewById(R.id.firstname_window);
            fw.setText(fw.getText().toString()+firstName);
            TextView lw = pw.getContentView().findViewById(R.id.lastname_window);
            lw.setText(lw.getText().toString()+lastName);
            TextView bdw = pw.getContentView().findViewById(R.id.birthdate_window);
            bdw.setText(bdw.getText().toString()+DateFormatter.formatForUi(birthDate));
            TextView gw = pw.getContentView().findViewById(R.id.gender_window);
            gw.setText(gw.getText().toString()+((gender == User.GENDER_FEMALE)?"Female":"Male"));
            TextView aw = pw.getContentView().findViewById(R.id.address_window);
            if(address.length()!=0)
                aw.setText(aw.getText().toString()+address);
            else
                aw.setText(aw.getText().toString()+"-");
            TextView phw = pw.getContentView().findViewById(R.id.phone_window);
            phw.setText(phw.getText().toString()+phone.getText().toString());

            TextView ew = pw.getContentView().findViewById(R.id.email_window);
            //if(email.getText().toString().length() != 0)
                ew.setText(ew.getText().toString()+email.getText().toString());
            //else
            //    ew.setText(ew.getText().toString()+"-");

            TextView sw = pw.getContentView().findViewById(R.id.single_window);
            sw.setText(sw.getText().toString()+((single)? "Single" : "Married"));
            

            //final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            //popupWindow.dismiss();
            pw.setFocusable(true);
            pw.update();
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);

        }
    }


}
