package com.mygdx.game;

import java.io.Serializable;

/**
 * Created by StriBog on 5/24/2017.
 */

public class SerializablePlayer implements Serializable {
    public SerializablePlayer(int ID,String log,String pas,String ema,String ava){
        id = ID;
        login = log;
        password = pas;
        email = ema;
        avatar = ava;
    }
    int id;
    String login;
    String password;
    String email;
    String avatar;
}
