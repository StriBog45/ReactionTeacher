package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by StriBog on 3/14/2017.
 */

public abstract class PointHit {
    private int x;
    private int y;
    private int radius;
    int number;
    int score;
    protected double time;
    Texture hitCircle;
    Texture hitCircleOverlay;
    Texture approachCircle;
    Texture textureNumber;
    Texture hit0;
    Texture hit50;
    Texture hit100;
    Texture hit300;
    Texture piece;
    SpriteBatch batch;
    float colorR;
    float colorG;
    float colorB;
    float colorA;
    float speed;
    float mouseX;
    float mouseY;
    boolean endLife;
    boolean end;
    boolean permissionDraw;
    boolean permissionMove;
    boolean checkSlider = false;

    abstract void draw();
    abstract void tick();
    abstract void touchDown();
    abstract void setScore();
    void checkPermission(long timeMillisecond){
        if(timeMillisecond == time)
            permissionDraw = true;
    };
    void checkHitting(int screenX,int screenY){
        if (!end && permissionDraw)
        {
            if ((screenX - x) * (screenX - x) + ((Gdx.graphics.getHeight() - screenY) - y) * ((Gdx.graphics.getHeight() - screenY) - y) < radius * radius)
                touchDown();
        }
    }
    int getX(){return x;};
    int getY(){return y;};
    int getRadius(){return radius;};
    void setX(int temp){x = temp;};
    void setY(int temp){y = temp;};
    void setRadius(int temp){radius = temp;};
}
