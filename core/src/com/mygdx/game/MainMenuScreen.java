package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

/**
 * Created by StriBog on 2/16/2017.
 */

public class MainMenuScreen extends UserScene{
    final ReactionTeacher game;
    OrthographicCamera camera;
    Texture desktopImage;
    Texture buttonImage;
    Vector3 touchPos;
    float xProfile;
    float yProfile;
    float widthProfile;
    float heightProfile;

    public ArrayList<Button> buttons;

    public void createButtons(){
        buttonImage = new Texture("BlueSquareButton.png");

        buttons.add(new Button(new Rectangle(game.width/10,game.height/16*8,game.width/10*3,game.height/32*3),buttonImage,"Играть!") {
            @Override
            public void func() {
                dispose();
                game.setUserScene(new SongsMenu(game));
                //Song tempSong = new Song();
                //game.setUserScene(new MainGame(game,tempSong));
            }
        });
        buttons.add(new Button(new Rectangle(game.width/10,game.height/16*6,game.width/10*3,game.height/32*3),buttonImage,"Тест реакции") {
            @Override
            public void func() {
                dispose();
                game.setUserScene(new TestReaction(game));
            }
        });
        buttons.add(new Button(new Rectangle(game.width/10,game.height/16*4,game.width/10*3,game.height/32*3),buttonImage,"Авторизация") {
            @Override
            public void func() {
                dispose();
                game.setUserScene(new AuthorizationMenu(game));
            }
        });
        if(game.typePlatform == 0) // только для пк
        buttons.add(new Button(new Rectangle(game.width/10,game.height/16*2,game.width/10*3,game.height/32*3),buttonImage,"Выход") {
            @Override
            public void func() {
                dispose();
                MainMenuScreen.super.dispose();
                Gdx.app.exit();
            }
        });
    }

    public MainMenuScreen(final ReactionTeacher gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, gam.width, gam.height);
        buttons= new ArrayList<Button>();
        touchPos = new Vector3();
        createButtons();
        game.setInput();

        desktopImage = new Texture("Desktop.jpg");

        xProfile = game.width - game.width/4;
        yProfile = game.height - game.height/14;
        widthProfile = game.width/16;
        heightProfile = game.height/16;
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


        if(game.online){
            game.batch.draw(game.avatar,xProfile,yProfile, widthProfile, heightProfile);
            game.font.draw(game.batch,game.login,xProfile+widthProfile,yProfile +heightProfile);
        }
        else
            game.font.draw(game.batch,"Оффлайн",xProfile+widthProfile,yProfile);

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
            if(b.rect.contains(touchPos.x,/*Gdx.graphics.getHeight()-*/touchPos.y)){
                b.func();
            }
        }
        return false;
    }
}
