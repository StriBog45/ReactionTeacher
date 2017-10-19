package com.mygdx.game;

import java.io.Serializable;

/**
 * Created by StriBog on 6/17/2017.
 */

public class SerializableSong implements Serializable {
    int id;
    String name;
    String author;
    boolean easy;
    boolean medium;
    boolean hard;
    boolean insane;
    String music;
    String background;
    SerializableSong(int Id,
                     String Name,
                     String Author,
                     boolean Easy,
                     boolean Medium,
                     boolean Hard,
                     boolean Insane,
                     String Music,
                     String Background){
        id = Id;
        name = Name;
        author = Author;
        easy = Easy;
        medium = Medium;
        hard = Hard;
        insane = Insane;
        music = Music;
        background = Background;

    }
}
