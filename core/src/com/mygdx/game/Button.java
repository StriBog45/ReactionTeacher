package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by StriBog-Notebook on 21.02.2017.
 */

public abstract class Button {
    Rectangle rect;
    Texture texture;
    String text;
    float textX;
    float textY;
    public Button(Rectangle r,Texture tr,String tempText){
        text = tempText;
        rect=r;
        texture = tr;
        textX = rect.x+rect.width/10;
        textY = rect.y+rect.height/3*2;
    }
    public abstract void func();
    public void draw(Batch batch,BitmapFont font){
        batch.begin();
        batch.draw(texture,rect.x,rect.y,rect.width,rect.height);
        font.draw(batch, text, textX,textY);
        batch.end();
    }
    public void changeHeight(float heightY){
        rect.y = heightY;
        textY = rect.y+rect.height/3*2;
    }
    public void touchDown(float touchX,float touchY){
        if(rect.contains(touchX,touchY))
            func();
    }

}
