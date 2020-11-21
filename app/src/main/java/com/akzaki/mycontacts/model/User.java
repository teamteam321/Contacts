package com.akzaki.mycontacts.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.akzaki.mycontacts.util.DateConverter;

import java.util.Date;
@Entity(tableName = "users_2")
public class User {
    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;

    @PrimaryKey(
            autoGenerate = true
    )
    public final int id;
    @ColumnInfo(name = "first_name")
    public final String firstName;
    @ColumnInfo(name = "last_name")
    public final String lastName;
    @ColumnInfo(name = "birth_date")
    @TypeConverters(DateConverter.class)
    public final Date birthDate;

    public final String phone;
    public final String address;
    public final String email;

    public final int gender;
    public final boolean single;

    public User(int id, String firstName, String lastName, Date birthDate, String phone, String address, String email, int gender, boolean single) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.single = single;
        //this.timeTest = timeTest;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", single=" + single +
                '}';
    }
}
