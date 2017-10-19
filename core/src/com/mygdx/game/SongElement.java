package com.mygdx.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by StriBog-Notebook on 23.03.2017.
 */

public abstract class SongElement {
    int maxCombo;
    int amountPeople;
    Rectangle rect;
    Texture texture;
    String text;
    boolean select;
    Music music;
    PersonScore []topScore;
    PersonScore selfScore;

    SongElement(Song song){
        rect = new Rectangle();
        texture = song.background;
        text = song.name;
        select = false;
        music = song.music;
        topScore = song.topScore;
        selfScore = song.selfScore;
        maxCombo = song.maxCombo;
        amountPeople = song.amountPeople;
    }
    public abstract void func();

    void draw(){

    }
}
