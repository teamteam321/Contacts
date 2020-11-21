package com.akzaki.mycontacts.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.akzaki.mycontacts.model.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users_2")
    User [] getAllUser();

    @Query("SELECT * FROM users_2 WHERE ID = :id")
    User getUserById(int id);

    @Update
    Void updateUser(User... u);

    @Insert
    void addUser(User... u);

    @Delete
    void deleteUser(User u);

}
