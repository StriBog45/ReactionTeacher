package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;

/**
 * Created by StriBog on 3/9/2017.
 */

public class PointSlider extends PointHit{
    private int x2;
    private int y2;
    float activeX;
    float activeY;
    int approachR;
    int timerEnd;
    float approachA;
    double angle;
    float []cordMid;
    float []cordTop;
    float []cordDown;
    PolygonSprite polyMid;
    PolygonSprite polyTop;
    PolygonSprite polyDown;
    PolygonSpriteBatch polyBatch;
    PolygonRegion polyRegMid;
    PolygonRegion polyRegTop;
    PolygonRegion polyRegDown;


    ShapeRenderer sr;

    int getDirection() {
        double degrees = Math.toDegrees(angle);
        double degreesMove = (degrees + 45);
        if(degreesMove > 360)
            degreesMove -= 360;
        double z = degreesMove / 90;
        int temp = (int)z;
            return temp;
    }
    int getDirection90(){
        double degrees = Math.toDegrees(angle);
        double z = degrees / 90;
        int temp = (int)z;
        return temp;
    }
    void setCords(){
        // Построение полигона
        cordMid = new float[10];
        cordTop = new float[10];
        cordDown = new float[10];
        if(getDirection() == 0 || getDirection() == 2){
            cordMid[0] = getX();
            cordMid[1] = getY() - getRadius() + getRadius()/8;
            cordMid[2] = getX();
            cordMid[3] = getY() + getRadius() - getRadius()/8;
            cordMid[4] = x2;
            cordMid[5] = y2 + getRadius() - getRadius()/8;
            cordMid[6] = x2;
            cordMid[7] = y2 - getRadius() + getRadius()/8;
            cordMid[8] = getX();
            cordMid[9] = getY() - getRadius() + getRadius()/8;

            cordTop[0] = getX();
            cordTop[1] = getY() + getRadius()/8*6;
            cordTop[2] = getX();
            cordTop[3] = getY() + getRadius() - getRadius()/8;
            cordTop[4] = x2;
            cordTop[5] = y2 + getRadius() - getRadius()/8;
            cordTop[6] = x2;
            cordTop[7] = y2 + getRadius()/8*6;
            cordTop[8] = getX();
            cordTop[9] = getY() + getRadius()/8*6;

            cordDown[0] = getX();
            cordDown[1] = getY() - getRadius() + getRadius()/8;
            cordDown[2] = getX();
            cordDown[3] = getY() - getRadius()/8*6;
            cordDown[4] = x2;
            cordDown[5] = y2 - getRadius()/8*6;
            cordDown[6] = x2;
            cordDown[7] = y2 - getRadius() + getRadius()/8;
            cordDown[8] = getX();
            cordDown[9] = getY() - getRadius() + getRadius()/8;
        }
        else{
            cordMid[0] = getX() - getRadius() + getRadius()/8;
            cordMid[1] = getY();
            cordMid[2] = getX() + getRadius() - getRadius()/8;
            cordMid[3] = getY();
            cordMid[4] = x2 + getRadius() - getRadius()/8;
            cordMid[5] = y2;
            cordMid[6] = x2 - getRadius() + getRadius()/8;
            cordMid[7] = y2;
            cordMid[8] = getX() - getRadius() + getRadius()/8;
            cordMid[9] = getY();

            cordTop[0] = getX() + getRadius()/8*6;
            cordTop[1] = getY();
            cordTop[2] = getX() + getRadius() - getRadius()/8;
            cordTop[3] = getY();
            cordTop[4] = x2 + getRadius() - getRadius()/8;
            cordTop[5] = y2;
            cordTop[6] = x2 + getRadius()/8*6;
            cordTop[7] = y2;
            cordTop[8] = getX() + getRadius()/8*6;
            cordTop[9] = getY();

            cordDown[0] = getX()- getRadius() + getRadius()/8;
            cordDown[1] = getY();
            cordDown[2] = getX()- getRadius()/8*6;
            cordDown[3] = getY();
            cordDown[4] = x2 - getRadius()/8*6;
            cordDown[5] = y2;
            cordDown[6] = x2 - getRadius() + getRadius()/8;
            cordDown[7] = y2;
            cordDown[8] = getX() - getRadius() + getRadius()/8;
            cordDown[9] = getY();
        }
    }
    void findAngle(){
        //вычисление угла
        if(getY() < y2 && getX() < x2) // 1
            angle = Math.atan((double)(getY() - y2) / (getX() - x2));
        if(getY() < y2 && getX() > x2){ // 2
            angle = Math.atan((double)(getY() - y2) / (x2 - getX()));
            angle = Math.PI - angle;
        }
        if(getY() > y2 && getX() > x2){ // 3
            angle = Math.atan((double)(y2 - getY()) / (x2 - getX()));
            angle += Math.PI;
        }
        if(getY() > y2 && getX() < x2){ // 4
            angle = Math.atan((double)(y2 - getY()) / (getX() - x2));
            angle = Math.PI*2 - angle;
        }
        if(getY() < y2 && getX() == x2 ) // 90
            angle = Math.PI / 2;
        if(getY() > y2 && getX() == x2) // 270
            angle = Math.PI + Math.PI / 2;
        if(getY() == y2 && getX() > x2) // 180
            angle = Math.PI;
        if(getY() == y2 && getX() < x2) // 0
            angle = 0;
    }
    void createPolygon(){
        TextureRegion temp = new TextureRegion(new Texture("1x1.png"));
        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(cordMid);
        polyRegMid = new PolygonRegion(temp, cordMid,triangleIndices.toArray());
        polyMid = new PolygonSprite(polyRegMid);
        polyMid.setOrigin(250, 250);

        triangulator = new EarClippingTriangulator();
        triangleIndices = triangulator.computeTriangles(cordTop);
        polyRegTop = new PolygonRegion(temp, cordTop,triangleIndices.toArray());
        polyTop = new PolygonSprite(polyRegTop);
        polyTop.setOrigin(250, 250);

        triangulator = new EarClippingTriangulator();
        triangleIndices = triangulator.computeTriangles(cordDown);
        polyRegDown = new PolygonRegion(temp, cordDown,triangleIndices.toArray());
        polyDown = new PolygonSprite(polyRegDown);
        polyDown.setOrigin(250, 250);
    }
    void setScore(){
        if(!endLife) {
            if (getRadius() * 2 > approachR) {
                score = 50;
                if (getRadius() * 1.5 > approachR) {
                    score = 100;
                    if (getRadius() * 1.2 > approachR) {
                        score = 300;
                    }
                }
            } else
                score = 0;
            endLife = true;
        }
    }
    boolean checkHitting(){
        if((mouseX - activeX) * (mouseX - activeX) + (mouseY - activeY) * (mouseY - activeY) < approachR * approachR)
            return true;
        else
            return false;
    }
    PointSlider(int tempX, int tempY, int r, double tempTime,float tempSpeeed, int tempX1, int tempY1){
        checkSlider = true;
        setX(tempX);
        setY(tempY);
        activeX = tempX;
        activeY = tempY;
        setRadius(r);
        speed = tempSpeeed;
        x2 = tempX1;
        y2 = tempY1;
        approachR = getRadius()*3;
        approachA = (float)0.5;
        time = tempTime;
        sr= new ShapeRenderer();
        boolean end = false;
        permissionDraw = false;
        score = 0;
        timerEnd = 5;
        number = 1;
        colorR = 1;
        colorB = 1;
        colorG = 0;
        colorA = 1;
        endLife = false;
        permissionMove = false;
        //вычисление угла
        findAngle();
        // Установка координат полигона
        setCords();
        // формирование фигуры
        createPolygon();

        polyBatch = new PolygonSpriteBatch();
    }

    void draw(){
        if(permissionDraw && !endLife) {
            polyBatch.begin();
            polyMid.setColor(colorR, colorG, colorB, colorA);
            polyMid.draw(polyBatch);
            polyTop.draw(polyBatch);
            polyDown.draw(polyBatch);
            polyBatch.end();

            batch.begin();
            batch.setColor(colorR, colorG, colorB, colorA);
            batch.draw(hitCircle, getX() - getRadius(), getY() - getRadius(), getRadius() * 2, getRadius() * 2);
            batch.draw(hitCircle, x2 - getRadius(), y2 - getRadius(), getRadius() * 2, getRadius() * 2);

            batch.setColor(Color.WHITE);
            batch.draw(hitCircleOverlay, getX() - getRadius(), getY() - getRadius(), getRadius() * 2, getRadius() * 2);
            batch.draw(hitCircleOverlay, x2 - getRadius(), y2 - getRadius(), getRadius() * 2, getRadius() * 2);

            batch.setColor(colorR,colorG,colorB,colorA);
            batch.draw(hitCircle, activeX - getRadius(), activeY - getRadius(), getRadius() * 2, getRadius() * 2);
            batch.setColor(Color.WHITE);
            batch.draw(hitCircleOverlay, activeX - getRadius(), activeY - getRadius(), getRadius() * 2, getRadius() * 2);
            batch.draw(textureNumber, activeX - getRadius() / 2, activeY - getRadius() / 2, getRadius(), getRadius());

            batch.setColor(colorR, colorG, colorB, approachA);
            batch.draw(approachCircle, activeX - approachR, activeY - approachR, approachR * 2, approachR * 2);
            batch.end();
        }
        if(endLife == true) {
            batch.begin();
            batch.setColor(Color.WHITE);
            if(score == 0)
                batch.draw(hit0,x2-getRadius(),y2-getRadius(),getRadius()*2,getRadius()*2);
            if(score == 50)
                batch.draw(hit50,x2-getRadius(),y2-getRadius(),getRadius()*2,getRadius()*2);
            if(score == 100)
                batch.draw(hit100,x2-getRadius(),y2-getRadius(),getRadius()*2,getRadius()*2);
            if(score == 300)
                batch.draw(hit300,x2-getRadius(),y2-getRadius(),getRadius()*2,getRadius()*2);
            batch.end();
        }

    }
    void touchDown(){

    }
    void tick(){
        if(approachR < getRadius()) {
            endLife = true;
        }



        // Движение
        if(permissionMove && checkHitting()) {
            activeX += speed * Math.cos(angle) * 1.5;
            activeY += speed * Math.sin(angle) * 1.5;

            if (getDirection90() == 0)
                if (activeY >= y2 && activeX >= x2)
                    setScore();
            if (getDirection90() == 1)
                if (activeY >= y2 && activeX <= x2)
                    setScore();
            if (getDirection90() == 2)
                if (activeY <= y2 && activeX <= x2)
                    setScore();
            if (getDirection90() == 3)
                if (activeY <= y2 && activeX >= x2)
                    setScore();
        }
        else
            approachR -= speed;

        // уменьшение прозрачности
        if(approachA <= 1)
            approachA += 0.05;

        // время показа счета
        if(endLife) {
            if(timerEnd <= 0)
                end = true;
            timerEnd--;
        }
    }
}