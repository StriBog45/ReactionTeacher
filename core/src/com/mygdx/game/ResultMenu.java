package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by StriBog on 4/10/2017.
 */

public class ResultMenu extends UserScene{
    final ReactionTeacher game;
    Song song;
    OrthographicCamera camera;
    Vector3 touchPos;
    Texture textureBackSongMenu;
    Texture textureRetry;
    Texture texturePiece;
    Button buttonBackSongMenu;
    Button buttonRetry;
    Rectangle rectangleScore;
    Rectangle rectangleSymbol;
    float scoreX;
    float scoreY;
    float comboX;
    float comboY;
    int amount300;
    int amount100;
    int amount50;
    int amount0;
    float accuracyX;
    float accuracyY;
    float amount300X;
    float amount300Y;
    float amount100X;
    float amount100Y;
    float amount50X;
    float amount50Y;
    float amount0X;
    float amount0Y;
    ResultMenu(final ReactionTeacher gam,Song tempSong){
        song = tempSong;
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, gam.width, gam.height);
        textureBackSongMenu = new Texture("backMainMenu.png");
        texturePiece = new Texture("1x1.png");
        textureRetry = textureBackSongMenu;
        touchPos = new Vector3();
        game.setTextureSymbol(game.selfScore);

        calculateAmounts(); // подсчет выбитых элементов

        // координаты
        rectangleScore = new Rectangle(game.width/10,game.height/10,game.width/10*8,game.height/10*8);
        rectangleSymbol = new Rectangle(game.width/10,game.height/10*1.5f,game.width/10*2f,game.height/10*5);
        accuracyX = rectangleSymbol.x + rectangleSymbol.width + 2;
        accuracyY = rectangleSymbol.y + rectangleSymbol.height;
        comboX = accuracyX;
        comboY = accuracyY - rectangleScore.height/15;
        amount300X = comboX;
        amount300Y = comboY - rectangleScore.height/15;
        amount100X = amount300X;
        amount100Y = amount300Y - rectangleScore.height/15;
        amount50X = amount100X;
        amount50Y = amount100Y - rectangleScore.height/15;
        amount0X = amount50X;
        amount0Y = amount50Y - rectangleScore.height/15;
        scoreX = rectangleScore.x + rectangleScore.width/3;
        scoreY = rectangleScore.y + rectangleScore.height - rectangleScore.height/6;

        game.selfScore.setPosition(new Rectangle(5,5,5,5));
        game.selfScore.rectangleAvatar.x = rectangleScore.x + rectangleScore.width/3;
        game.selfScore.rectangleAvatar.y = rectangleScore.y + rectangleScore.height - rectangleScore.height/8;
        game.selfScore.rectangleAvatar.width = rectangleScore.width / 12;
        game.selfScore.rectangleAvatar.height = (rectangleScore.y + rectangleScore.height) - game.selfScore.rectangleAvatar.y - 2;
        game.selfScore.cordNameX = game.selfScore.rectangleAvatar.x + game.selfScore.rectangleAvatar.width + 2;
        game.selfScore.cordNameY = game.selfScore.rectangleAvatar.y + game.selfScore.rectangleAvatar.height/1.5f;

        for(int i=0; i<song.topScore.length; i++) {
            game.setTextureSymbol(song.topScore[i]);
            song.topScore[i].setPosition(new Rectangle(accuracyX+rectangleScore.width/4.3f, accuracyY-(game.height/12*i)-game.height/12-(i*2), (rectangleScore.x+rectangleScore.width)-(accuracyX+rectangleScore.width/4.3f), game.height/12));
        }
        song.selfScore.setPosition(new Rectangle(accuracyX+rectangleScore.width/4.3f, accuracyY-(game.height/12*5)-game.height/12-(5*2), (rectangleScore.x+rectangleScore.width)-(accuracyX+rectangleScore.width/4.3f), game.height/12));

        buttonBackSongMenu = new Button(new Rectangle(0,0, game.width/10*3, game.height/8),textureBackSongMenu,"Назад") {
            @Override
            public void func() {
                dispose();
                game.batch.setColor(Color.WHITE);
                game.setUserScene(new SongsMenu(game));
            }
        };
        buttonRetry = new Button(new Rectangle(game.width - game.width/10*3,0, game.width/10*3, game.height/8),textureBackSongMenu,"Повторить") {
            @Override
            public void func() {
                dispose();
                game.batch.setColor(Color.WHITE);
                game.setUserScene(new MainGame(game,song));
            }
        };
    }

    public void calculateAmounts(){
        for (PointHit p:song.pointHits) {
            if(p.score == 300)
                amount300++;
            if(p.score == 100)
                amount100++;
            if(p.score == 50)
                amount50++;
            if(p.score == 0)
                amount0++;
        }
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);  // это преобразование координат нажатия в рабочие координаты (координаты камеры)
        if(buttonBackSongMenu.rect.contains(touchPos.x,touchPos.y))
            buttonBackSongMenu.func();
        if(buttonRetry.rect.contains(touchPos.x,touchPos.y))
            buttonRetry.func();
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        if(song.background != null)
            game.batch.draw(song.background,0,0,game.width,game.height);

        // тёмно синий прямоугольник
        game.batch.setColor(0f,0f,0.2f,0.8f);
        game.batch.draw(texturePiece,rectangleScore.x,rectangleScore.y,rectangleScore.width,rectangleScore.height);
        game.batch.setColor(Color.WHITE);

        // кнопки
        game.batch.draw(buttonBackSongMenu.texture,buttonBackSongMenu.rect.x,buttonBackSongMenu.rect.y,buttonBackSongMenu.rect.width,buttonBackSongMenu.rect.height);
        game.font.draw(game.batch,buttonBackSongMenu.text,buttonBackSongMenu.textX,buttonBackSongMenu.textY);
        game.batch.draw(buttonRetry.texture,buttonRetry.rect.x,buttonRetry.rect.y,buttonRetry.rect.width,buttonRetry.rect.height);
        game.font.draw(game.batch,buttonRetry.text,buttonRetry.textX,buttonRetry.textY);

        // символ
        game.batch.draw(game.selfScore.textureSymbol,rectangleSymbol.x,rectangleSymbol.y,rectangleSymbol.width,rectangleSymbol.height);

        //счет
        game.fontLarge.draw(game.batch,"Счет " + game.selfScore.score,scoreX,scoreY);
        game.fontSmall.draw(game.batch,"Точность " + game.selfScore.accuracy +"%",accuracyX,accuracyY);
        game.fontSmall.draw(game.batch,"Комбо          " + game.selfScore.combo+"x",comboX,comboY);
        game.fontSmall.draw(game.batch,"300                  " + amount300 + "x",amount300X,amount300Y);
        game.fontSmall.draw(game.batch,"100                  " + amount100 + "x",amount100X,amount100Y);
        game.fontSmall.draw(game.batch,"50                    " + amount50 + "x",amount50X,amount50Y);
        game.fontSmall.draw(game.batch,"0                      " + amount0 + "x",amount0X,amount0Y);

        // Свой аватар, ник, ранк
        game.batch.draw(game.selfScore.avatar,game.selfScore.rectangleAvatar.x,game.selfScore.rectangleAvatar.y,game.selfScore.rectangleAvatar.width,game.selfScore.rectangleAvatar.height);
        game.font.draw(game.batch,"Ник " + game.selfScore.name,game.selfScore.cordNameX,game.selfScore.cordNameY);
        //game.font.draw(game.batch,"Позиция " + game.selfScore.rank,game.selfScore.cordRankX,game.selfScore.cordRankY);

        //рекорды
        for(int i=0; i<song.topScore.length; i++)
        {
            // Отрисовка персон
            game.batch.setColor(1,1,1,0.2f);
            game.batch.draw(texturePiece,song.topScore[i].rectanglePerson.x,song.topScore[i].rectanglePerson.y,song.topScore[i].rectanglePerson.width,song.topScore[i].rectanglePerson.height);
            game.batch.setColor(Color.WHITE);
            game.batch.draw(song.topScore[i].avatar,song.topScore[i].rectangleAvatar.x,song.topScore[i].rectangleAvatar.y,song.topScore[i].rectangleAvatar.width,song.topScore[i].rectangleAvatar.height);
            game.batch.draw(song.topScore[i].textureSymbol, song.topScore[i].rectangleSymbol.x, song.topScore[i].rectangleSymbol.y, song.topScore[i].rectangleSymbol.width, song.topScore[i].rectangleSymbol.height);
            game.fontVerySmall.draw(game.batch, song.topScore[i].name + " Позиция " + song.topScore[i].rank, song.topScore[i].cordNameX,song.topScore[i].cordNameY);
            game.fontVerySmall.draw(game.batch, "Комбо " + song.topScore[i].combo + " Точность " + song.topScore[i].accuracy + "%", song.topScore[i].cordComboX, song.topScore[i].cordComboY);
            game.fontVerySmall.draw(game.batch, "Счет " + song.topScore[i].score, song.topScore[i].cordScoreX, song.topScore[i].cordScoreY);
        }
        // Отрисовка себя
        game.batch.setColor(1,1,1,0.6f);
        game.batch.draw(texturePiece,song.selfScore.rectanglePerson.x,song.selfScore.rectanglePerson.y,song.selfScore.rectanglePerson.width,song.selfScore.rectanglePerson.height);
        game.batch.setColor(Color.WHITE);
        game.batch.draw(song.selfScore.avatar,song.selfScore.rectangleAvatar.x,song.selfScore.rectangleAvatar.y,song.selfScore.rectangleAvatar.width,song.selfScore.rectangleAvatar.height);
        game.batch.draw(song.selfScore.textureSymbol, song.selfScore.rectangleSymbol.x, song.selfScore.rectangleSymbol.y, song.selfScore.rectangleSymbol.width, song.selfScore.rectangleSymbol.height);
        game.fontVerySmall.draw(game.batch, song.selfScore.name + " Позиция " + song.selfScore.rank, song.selfScore.cordNameX,song.selfScore.cordNameY);
        game.fontVerySmall.draw(game.batch, "Комбо " + song.selfScore.combo + " Точность " + song.selfScore.accuracy + "%", song.selfScore.cordComboX, song.selfScore.cordComboY);
        game.fontVerySmall.draw(game.batch, "Счет " + song.selfScore.score, song.selfScore.cordScoreX, song.selfScore.cordScoreY);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
        textureBackSongMenu.dispose();
        texturePiece.dispose();
    }
}
