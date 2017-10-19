package com.mygdx.game;

/**
 * Created by StriBog on 2/16/2017.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.touchDown;

public class TestReaction  extends UserScene {
    final ReactionTeacher game;
    OrthographicCamera camera;
    Rectangle bucket;
    Vector3 touchPos;
    Array<Rectangle> raindrops;
    long lastStartTime;
    long lastMillisecond;
    long reactionTime;
    String dropString;
    ShapeRenderer sr;
    boolean start;
    boolean changeColor;


    public TestReaction (final ReactionTeacher gam) {
        this.game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        touchPos = new Vector3();
        sr= new ShapeRenderer();
        start = false;
        changeColor = false;
        lastMillisecond = TimeUtils.nanoTime();
        reactionTime = 0;

    }

    @Override
    public void render (float delta) {
        if(!changeColor) {
            Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        if(changeColor) {
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        if(!start) {
            game.batch.begin();
            game.font.draw(game.batch, "Нажмите на экран для запуска теста", 150, 250);
            game.font.draw(game.batch, "Ваша скорость реакции: " + String.valueOf(reactionTime/1000000000), 150, 200);
            game.batch.end();
        }
        if(start)
        {
            game.batch.begin();
            game.font.draw(game.batch, "Нажмите на экран когда экран поменяет цвет", 150, 250);
            game.batch.end();

            if (TimeUtils.nanoTime() - lastStartTime > 1000000000 && !changeColor) {
                changeColor = true;
            }
        }


    }

    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void pause() {
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(start)
        {
            if(!changeColor)
            {
                // нажали раньше смены цвета
                Gdx.gl.glClearColor(0, 0, 0.2f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                reactionTime = TimeUtils.nanoTime();
                start = false;
                return super.touchDown(screenX, screenY, pointer, button);
            }
            if(changeColor)
            {
                // цвет другой
                reactionTime =  TimeUtils.nanoTime() - reactionTime;
                Gdx.gl.glClearColor(0, 0, 0.2f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                start = false;
                changeColor = false;
                return super.touchDown(screenX, screenY, pointer, button);
            }
        }
        if(!start){
            start = true;
            reactionTime = 0;
            lastStartTime = TimeUtils.nanoTime();
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void resume() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void dispose() {
    }

    @Override
    public void show() {

    }
}