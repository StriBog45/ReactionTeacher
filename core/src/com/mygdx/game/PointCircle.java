package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * Created by StriBog on 2/20/2017.
 */

public class PointCircle extends PointHit{
    int approachR;
    int timerEnd;

    private float approachA;


    ShapeRenderer sr;

    PointCircle(int tempX, int tempY, int r, double tempTime, float tempSpeed){
        setX(tempX);
        setY(tempY);
        setRadius(r);
        speed = tempSpeed;
        approachR = getRadius()*3;
        approachA = (float)0.5;
        time = tempTime;
        sr= new ShapeRenderer();
        boolean end = false;
        permissionDraw = false;
        permissionMove = false;
        score = 0;
        timerEnd = 5;
        number = 1;
        colorR = 1;
        colorB = 1;
        colorG = 0;
        colorA = 1;
        endLife = false;
    }
    void setScore(){
        if(!endLife) {
            if (getRadius() * 1.5 > approachR) {
                score = 50;
                if (getRadius() * 1.3 > approachR) {
                    score = 100;
                    if (getRadius() * 1.1 > approachR) {

                        score = 300;
                    }
                }
            }
            endLife = true;
        }
    }
    void draw(){
        if(permissionDraw && !endLife && !end) {
            batch.begin();
            batch.setColor(colorR, colorG, colorB, colorA);
            batch.draw(hitCircle, getX() - getRadius(), getY() - getRadius(), getRadius() * 2, getRadius() * 2);

            batch.setColor(Color.WHITE);
            batch.draw(hitCircleOverlay, getX() - getRadius(), getY() - getRadius(), getRadius() * 2, getRadius() * 2);
            batch.draw(textureNumber, getX() - getRadius() / 2, getY() - getRadius() / 2, getRadius(), getRadius());

            batch.setColor(colorR, colorG, colorB, approachA);
            batch.draw(approachCircle, getX() - approachR, getY() - approachR, approachR * 2, approachR * 2);
            batch.end();
        }
        if(endLife == true && !end) {
            batch.begin();
            if(score == 0)
                batch.draw(hit0,getX()-getRadius(),getY()-getRadius(),getRadius()*2,getRadius()*2);
            if(score == 50)
                batch.draw(hit50,getX()-getRadius(),getY()-getRadius(),getRadius()*2,getRadius()*2);
            if(score == 100)
                batch.draw(hit100,getX()-getRadius(),getY()-getRadius(),getRadius()*2,getRadius()*2);
            if(score == 300)
                batch.draw(hit300,getX()-getRadius(),getY()-getRadius(),getRadius()*2,getRadius()*2);
            batch.end();
        }

    }
    void touchDown(){
        setScore();
    }
    void tick(){
        if(endLife) {
            if(timerEnd <= 0)
                end = true;
            timerEnd -= speed;
        }
        else{
            if(approachR < getRadius()) {
                endLife = true;
            }
            approachR -= speed;
            if(approachA <= 1)
                approachA += 0.05;
        }
    }

}
