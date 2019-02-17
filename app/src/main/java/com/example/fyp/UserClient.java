package com.example.fyp;

import android.app.Application;
import com.example.fyp.User;

public class UserClient extends Application {

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}


