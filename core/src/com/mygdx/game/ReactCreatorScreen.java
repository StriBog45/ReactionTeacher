package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.sqlite.DatabaseHandler;
import com.badlogic.gdx.sqlite.DatabaseHandlerFactory;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StriBog on 2/16/2017.
 */

public class ReactCreatorScreen extends UserScene{
    final ReactionCreator game;
    OrthographicCamera camera;
    Texture desktopImage;
    Texture buttonImage;
    Vector3 touchPos;
    ArrayList<Integer> arrayX;
    ArrayList<Integer> arrayY;
    ArrayList<Integer> arrayTime;
    long lastMillisecond;
    long timeMillisecond;
    DatabaseHandler dbHandler;
    float speed = 5;
    int IDSong = 3;
    int Complexity = 2;
    int Radius = 80;
    int shift = (int)(Radius*2/speed);

    public static final String TABLE_PLAYERS = "Players";
    public static final String TABLE_SONGS = "Songs";
    public static final String TABLE_CIRCLE = "Circle";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_COMMENT = "Login";
    public static final int PORT = 7777;
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_PLAYERS + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_COMMENT + " text not null);";

    public ArrayList<Button> buttons;

    public void createButtons(){
        buttonImage = new Texture("BlueSquareButton.png");

        if(game.typePlatform == 0) // только для пк
        buttons.add(new Button(new Rectangle(0,0,game.width/10*3,game.height/32*3),buttonImage,"Выход") {
            @Override
            public void func() {
                arrayX.size();
                arrayY.size();
                arrayTime.size();
                //ReactCreatorScreen.super.dispose();


                dbHandler = DatabaseHandlerFactory.getNewDatabaseHandler(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);
                dbHandler.setupDatabase();
                dbHandler.openOrCreateDatabase();
                for(int i=0; i<arrayX.size(); i++) {
                    dbHandler.execSQL("INSERT INTO " + TABLE_CIRCLE + " (IDSong,Complexity,X,Y,Time,Speed,Radius) VALUES ('"+IDSong+"','"+Complexity+"','"+arrayX.get(i)+"','"+arrayY.get(i)+"','"+arrayTime.get(i)+"','"+speed+"','"+Radius+"')");
                }
                dbHandler.closeDatabae();

                dispose();
                Gdx.app.exit();
            }
        });
    }

    public ReactCreatorScreen(final ReactionCreator gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, gam.width, gam.height);
        buttons= new ArrayList<Button>();
        touchPos = new Vector3();
        createButtons();
        game.setInput();



        arrayTime = new ArrayList<Integer>();
        arrayX = new ArrayList<Integer>();
        arrayY = new ArrayList<Integer>();
        desktopImage = new Texture("Desktop.jpg");

        Music music = Gdx.audio.newMusic(Gdx.files.internal("Music/End Time.mp3"));


        timeMillisecond = 0;
        music.play();
        lastMillisecond = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        //game.batch.disableBlending(); // отключение прозрачности
        game.batch.draw(desktopImage,0,0, game.width, game.height);
        //game.batch.enableBlending();

        for(Button b:buttons){
            game.batch.draw(b.texture,b.rect.x,b.rect.y,b.rect.width,b.rect.height);
            game.font.draw(game.batch, b.text, b.textX, b.textY);
        }


        if (TimeUtils.nanoTime() - lastMillisecond > 10000000) {
            // 1 000 000 000 = секунда
            timeMillisecond++;
            lastMillisecond = TimeUtils.nanoTime();
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.width = width;
        game.height = height;

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
        desktopImage.dispose();
        buttonImage.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);  // это преобразование координат нажатия в рабочие координаты (координаты камеры)

        for(Button b:buttons){
            b.touchDown(touchPos.x,touchPos.y);
        }
        arrayTime.add((int)(timeMillisecond - shift));
        arrayX.add((int)touchPos.x);
        arrayY.add((int)touchPos.y);
        return false;
    }
}
