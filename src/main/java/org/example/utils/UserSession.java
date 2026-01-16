package org.example.utils;

import org.example.model.User;

public class UserSession {

    public static User currentUser = null;


    public static boolean checklogin(){
        return currentUser != null;
    }
}
