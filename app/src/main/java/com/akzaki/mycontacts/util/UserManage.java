package com.akzaki.mycontacts.util;

import com.akzaki.mycontacts.model.User;

public class UserManage {

    public static User[] sortUser(User[]arr){
        User [] nnn = arr;
        User temp;
        for(int e = 0;e<arr.length-1;e++){
            for(int r = e+1;r<arr.length;r++){
                if(arr[e].firstName.compareTo(arr[r].firstName) > 0){
                    temp = arr[e];
                    arr[e] = arr[r];
                    arr[r] = temp;
                }
            }
        }
        return nnn;
    }

}
