package com.mygdx.game;

/**
 * Created by StriBog on 2/16/2017.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import static com.badlogic.gdx.math.MathUtils.random;

public class MainGame extends UserScene {
    final ReactionTeacher game;
    OrthographicCamera camera;
    Texture piece;
    Texture texturePause;
    Texture textureMenu;
    Vector3 touchPos;
    long lastTickTime;
    long lastMillisecond;
    long timeMillisecond;
    Song song;
    float startTime;
    int numberPoint;
    int amountElements;
    int bestCombo;
    float colorR;
    float colorG;
    float colorB;
    float colorA;
    Button buttonPause;
    Button buttonContinue;
    Button buttonSongMenu;
    boolean pause;

    public MainGame(final ReactionTeacher gam, Song tempSong){
        game = gam;
        song = tempSong;
        pause = false;
        startTime = 0;
        numberPoint = 1;
        game.selfScore.combo = 0;
        game.selfScore.score = 0;
        game.selfScore.accuracy = 0;
        amountElements = 0;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, gam.width, gam.height);

        touchPos = new Vector3();
        piece = new Texture("1x1.png");
        texturePause = new Texture("pause.png");
        textureMenu = new Texture("BlueSquareButton.png");
        buttonPause = new Button(new Rectangle(0, game.height/10*9, game.width/12, game.height/10),texturePause,"") {
            @Override
            public void func() {
                pause = true;
                song.music.pause();
            }
        };
        buttonContinue = new Button(new Rectangle(game.width/10, game.height/6*3, game.width/10*8, game.height/6),textureMenu,"Продолжить") {
            @Override
            public void func() {
                pause = false;
                song.music.play();
            }
        };
        buttonSongMenu = new Button(new Rectangle(game.width/10, game.height/6*2, game.width/10*8, game.height/6),textureMenu,"В меню") {
            @Override
            public void func() {
                song.music.pause();
                dispose();
                game.setUserScene(new SongsMenu(game));
            }
        };

        // Установка цвета и чисел на поинты
        colorA = 1;
        colorR = 1;
        colorG = 0;
        colorB = 1;

        for (PointHit p: song.pointHits) {
            p.batch = game.batch;
            p.number = numberPoint;
            p.colorR = colorR;
            p.colorG = colorG;
            p.colorB = colorB;
            p.colorA = colorA;
            p.approachCircle = song.approachCircle;
            p.hitCircle = song.hitCircle;
            p.hitCircleOverlay = song.hitCircleOverlay;
            p.hit0 = song.hit0;
            p.hit50 = song.hit50;
            p.hit100 = song.hit100;
            p.hit300 = song.hit300;
            p.piece = piece;

            numberPoint++;

            if (numberPoint > 9) // если число в ячейке больше 9
                numberPoint = 1;
            p.textureNumber = song.number.get(p.number-1);
        }

        song.music.setLooping(false); // зацикливание песни
        song.music.play();
        startTime = Gdx.graphics.getDeltaTime();
        lastTickTime = TimeUtils.nanoTime(); // колдовство со временем
        lastMillisecond = TimeUtils.nanoTime();
        timeMillisecond = 0;
    }
    void calculateScore(){
        // подсчет очков,комбо,точности
        amountElements = 0;
        game.selfScore.accuracy = 0;
        game.selfScore.score = 0;
        game.selfScore.combo = 0;
        for(int i=0; i<song.pointHits.size; i++) {
            if (song.pointHits.get(i).end) {
                if (song.pointHits.get(i).score != 0)
                    game.selfScore.combo++;
                else {
                    if(game.selfScore.combo > bestCombo)
                        bestCombo = game.selfScore.combo;
                    game.selfScore.combo = 0;
                }

                if (game.selfScore.combo != 0)
                    game.selfScore.score += song.pointHits.get(i).score * game.selfScore.combo;
                else
                    game.selfScore.score += song.pointHits.get(i).score;

                if (song.pointHits.get(i).score == 300)
                    game.selfScore.accuracy++;
                amountElements++;
            }
        }
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!pause) {
            //(Px - Cx)^2 + (Py - Cy)^2 <= R^2. проверка попадания в круг
            for (int i = 0; i < song.pointHits.size; i++) {
                song.pointHits.get(i).checkHitting(screenX, screenY);
            }


            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);  // это преобразование координат нажатия в рабочие координаты (координаты камеры)
            if (buttonPause.rect.contains(touchPos.x,/*Gdx.graphics.getHeight()-*/touchPos.y)) {
                buttonPause.func();
            }
        }
        else{
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);  // это преобразование координат нажатия в рабочие координаты (координаты камеры)
            if (buttonContinue.rect.contains(touchPos.x,/*Gdx.graphics.getHeight()-*/touchPos.y)) {
                buttonContinue.func();
            }
            if (buttonSongMenu.rect.contains(touchPos.x,/*Gdx.graphics.getHeight()-*/touchPos.y)) {
                buttonSongMenu.func();
            }

        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void render (float delta) {
        startTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // подсчет очков       (полный перебор)
        calculateScore();
        game.batch.setProjectionMatrix(camera.combined);
        camera.update();

        game.batch.begin();
        game.batch.disableBlending();
        game.batch.setColor(Color.WHITE);
        if(song.background != null)
            game.batch.draw(song.background,0,0,game.width,game.height);
        game.batch.enableBlending();
        game.font.draw(game.batch, "Счет: " + game.selfScore.score, game.width/10*7, game.height - game.font.getLineHeight() /*game.height/11*10*/);
        game.font.draw(game.batch, "Комбо: " + game.selfScore.combo, 10, game.font.getLineHeight());
        if(amountElements != 0)
            game.font.draw(game.batch, "Точность: " + String.format("%.2f", game.selfScore.accuracy/amountElements * 100) + "%", game.width/10*7,game.height - game.font.getLineHeight()*2/*game.height/11*9*/);
        else
            game.font.draw(game.batch, "Точность: " + "0" + "%", game.width/10*7,game.height - game.font.getLineHeight()*2);
        game.batch.draw(buttonPause.texture,buttonPause.rect.x,buttonPause.rect.y,buttonPause.rect.width,buttonPause.rect.height);
        game.batch.end();

        boolean touched = false;
        if (Gdx.input.isTouched()) {
            touched = true;
        }

        for(PointHit p: song.pointHits)
        {
            if(!p.end) {
                // рисуем
                p.draw();
            }

        }

        if(!pause) { // tick, 0.01 секунды
            if (TimeUtils.nanoTime() - lastMillisecond > 10000000) {
                // 1 000 000 000 = секунда
                timeMillisecond++;
                lastMillisecond = TimeUtils.nanoTime();
                for (PointHit p : song.pointHits) {
                    p.checkPermission(timeMillisecond);
                    if (p.permissionDraw) {
                        if (p.checkSlider) { // тик только для слайдеров
                            p.permissionMove = touched;
                            /////////////////////////////////////////////////////////////////починить камеру
                            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                            //camera.unproject(touchPos);

                            p.mouseX = touchPos.x;
                            p.mouseY = Gdx.graphics.getHeight() - touchPos.y;
                            p.tick();
                            p.permissionMove = false;
                        } else
                            p.tick();
                    }
                }
            }

            // Если песня закончилась, то переход в меню результатов
            if(!song.music.isPlaying()) {
                dispose();
                if(bestCombo > game.selfScore.combo)
                    game.selfScore.combo = bestCombo;
                game.setUserScene(new ResultMenu(game,song));
            }
        }

        // отрисовка меню паузы
        if(pause){
            game.batch.begin();
            game.batch.setColor(Color.WHITE);
            game.batch.draw(buttonContinue.texture,buttonContinue.rect.x,buttonContinue.rect.y,buttonContinue.rect.width,buttonContinue.rect.height);
            game.font.draw(game.batch, buttonContinue.text, buttonContinue.rect.x+buttonContinue.rect.width/10, buttonContinue.rect.y+buttonContinue.rect.height/2);
            game.batch.draw(buttonSongMenu.texture,buttonSongMenu.rect.x,buttonSongMenu.rect.y,buttonSongMenu.rect.width,buttonSongMenu.rect.height);
            game.font.draw(game.batch, buttonSongMenu.text, buttonSongMenu.rect.x+buttonSongMenu.rect.width/10, buttonSongMenu.rect.y+buttonSongMenu.rect.height/2);
            game.batch.end();
        }

    }

    @Override
    public void resize(int width, int height) {
        game.width = width;
        game.height = height;
        camera.setToOrtho(false, game.width, game.height);
    }

    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void hide() {
    }
    @Override
    public void dispose() {
        piece.dispose();
        texturePause.dispose();
        textureMenu.dispose();
    }
    @Override
    public void show() {

    }
}
