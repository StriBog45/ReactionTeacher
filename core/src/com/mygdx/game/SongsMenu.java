package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.sqlite.DatabaseCursor;
import com.badlogic.gdx.sqlite.DatabaseHandler;
import com.badlogic.gdx.sqlite.DatabaseHandlerFactory;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by StriBog on 3/17/2017.
 */

public class SongsMenu extends UserScene {
    final ReactionTeacher game;
    Texture textureFrame;
    Texture desktop;
    Texture textureChooseDifficulty;
    Texture textureBackMainMenu;
    Texture texturePiece;
    Texture texturePlay;
    Texture textureDownload;
    Texture textureDelete;
    OrthographicCamera camera;
    Array<Song> songs;
    Array<Button> buttons;
    ArrayList<SongElement> songElements;
    SongElement selectSong;
    Vector3 touchPos;
    Table table;
    Music music;
    Button buttonBackSongMenu;
    Button buttonBackMainMenu;
    Button buttonMusicPlay;
    Button buttonDownloadMenu;
    Button buttonCancelDownload;
    Button buttonDelete;
    Button buttonYes;
    Button buttonNo;
    float yourRecordX;
    float yourRecordY;
    float havntRecordX;
    float havntRecordY;
    float mouseMove1;
    float mouseMove2;
    boolean haveSelfScore;
    boolean difficultyChoose;
    boolean downloadMenu;

    DatabaseHandler dbHandler;
    private static final String TABLE_SONGS = "Songs";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_COMMENT = "Login";
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_SONGS + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_COMMENT + " text not null);";

    SongsMenu(final ReactionTeacher gam){
        haveSelfScore = true;
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, gam.width, gam.height);
        table = new Table();
        touchPos = new Vector3();
        textureFrame = new Texture("frame.png");
        textureChooseDifficulty = new Texture("BlueSquareButton.png");
        textureBackMainMenu = new Texture("backMainMenu.png");
        texturePiece = new Texture("1x1.png");
        texturePlay = new Texture("play.png");
        textureDownload = new Texture("download.png");
        textureDelete = new Texture("delete.png");
        buttons = new Array<Button>();
        difficultyChoose = false;
        downloadMenu = false;

        loadSongs();
        createButtons();

        if(songElements != null)
            if(songElements.size() != 0)
                desktop = songElements.get(0).texture;

    }
    void loadSongs(){
        databaseConnect();

        // Персоны
        PersonScore selfScore = new PersonScore("StriBog",new Texture("noimage.png"),180000,130,100f,'A',17);
        yourRecordX = game.width/8;
        yourRecordY = game.height/3 + game.height/15;
        selfScore.setPositionColumn(new Rectangle(0, game.height/6, game.width/5*2, game.height/6));
        game.setTextureSymbol(selfScore);
        havntRecordX = selfScore.rectanglePerson.x + selfScore.rectanglePerson.width/3;
        havntRecordY = selfScore.rectanglePerson.y + selfScore.rectanglePerson.height/2;

        PersonScore []topScore = new PersonScore[5];
        topScore[0] = new PersonScore("Asemty",new Texture("bucket.png"),180000,130,100f,'S',1);
        topScore[1] = new PersonScore("Nikita",new Texture("noimage.png"),180000,130,100f,'A',2);
        topScore[2] = new PersonScore("IVP",new Texture("droplet.png"),180000,130,100f,'B',3);
        topScore[3] = new PersonScore("Luntik",new Texture("hit0.png"),180000,130,100f,'C',4);
        topScore[4] = new PersonScore("Evgusha",new Texture("hit50.png"),180000,130,100f,'D',5);
        for(int i=0; i<topScore.length; i++) {
            game.setTextureSymbol(topScore[i]);
            if(i == 0)
                topScore[i].setPosition(new Rectangle(0, game.height/12*(12-(i+1)), game.width/5*2, game.height/12));
            else
                topScore[i].setPosition(new Rectangle(0, game.height/12*(12-(i+1))-(2*i), game.width/5*2, game.height/12));
        }

        DatabaseCursor cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_SONGS);
        songs = new Array<Song>();
        boolean b = cursor.next();
        while (b) {
            songs.add(new Song(cursor.getInt(0),cursor.getString(1),cursor.getString(2),Boolean.parseBoolean(cursor.getString(3)),Boolean.parseBoolean(cursor.getString(4)),Boolean.parseBoolean(cursor.getString(5)),Boolean.parseBoolean(cursor.getString(6)),cursor.getString(7),cursor.getString(8),selfScore,topScore));
            b = cursor.next();
        }
        /*songs.add(new Song("Still Alive",new Texture("Music/Still Alive.jpg"),true,true,true,true,selfScore,topScore,1044,100));
        songs.add(new Song("River Flows In You",new Texture("Music/River Flows In You.jpg"),true,true,true,false,selfScore,topScore,280,100));
        songs.add(new Song("End Time",new Texture("Music/End Time.jpg"),true,true,true,false,selfScore,topScore,270,100));
        songs.add(new Song("Eternal Fantasy",new Texture("Music/Eternal Fantasy.jpg"),true,true,true,false,selfScore,topScore,240,100));
        songs.add(new Song("Owl City - Embers",new Texture("Music/Owl City - Embers.jpg"),true,true,true,false,selfScore,topScore,220,100));
        songs.add(new Song("Stay Stay Stay",new Texture("Music/Stay Stay Stay.jpg"),true,true,true,false,selfScore,topScore,448,100));
        songs.add(new Song("Shnapi",new Texture("Music/Shnapi.jpg"),true,false,false,false,selfScore,topScore,64,100));*/

        createSongButtons();
        moveElements(-1);
    }
    void loadServerSongs(){
        songs = new Array<Song>();
        if(music != null)
            music.stop();

        LipeConnect lipeConnect = new LipeConnect(game.serverIP,game.port);
        SerializableSong []serializableSong = lipeConnect.songs();

        if(serializableSong != null)
        for(int i=0; i<serializableSong.length; i++){
            songs.add(new Song(serializableSong[i].id,serializableSong[i].name,serializableSong[i].author,serializableSong[i].easy,serializableSong[i].medium,serializableSong[i].hard,serializableSong[i].insane,serializableSong[i].music,serializableSong[i].background));
        }

        createSongButtons();
        moveElements(-1);
    }

    void createButtons(){
        buttonBackSongMenu = new Button(new Rectangle(game.width/10, game.height/6*3, game.width/10*8, game.height/6),textureChooseDifficulty,"Назад") {
            @Override
            public void func() {
                difficultyChoose = false;
            }
        };
        buttonBackMainMenu = new Button(new Rectangle(0,0, game.width/10*3, game.height/8),textureBackMainMenu,"Назад") {
            @Override
            public void func() {
                dispose();
                game.batch.setColor(Color.WHITE);
                game.setUserScene(new MainMenuScreen(game));
            }
        };
        buttonMusicPlay = new Button(new Rectangle(game.width/3*2 - game.width/6,game.height/2, game.width/12, game.height/8),texturePlay,"") {
            @Override
            public void func() {
                if(music != null)
                    music.stop();
                music = selectSong.music;
                music.play();
            }
        };
        buttonDownloadMenu = new Button(new Rectangle(game.width/10*3,0, game.height/8, game.height/8),textureDownload,"") {
            @Override
            public void func() {
                downloadMenu = true;
                loadServerSongs();
            }
        };
        buttonDelete = new Button(new Rectangle(buttonDownloadMenu.rect.x+game.height/8,0, game.height/8, game.height/8),textureDelete,"") {
            @Override
            public void func() {

            }
        };
        buttonCancelDownload = new Button(new Rectangle(0,0, game.width/10*3, game.height/8),textureBackMainMenu,"Назад") {
            @Override
            public void func() {

                downloadMenu = false;
                if(music != null)
                    music.stop();
                loadSongs();
            }
        };
    }
    void createSongButtons(){
        songElements = new ArrayList<SongElement>();
        // добавление песен в меню
        for(final Song s: songs)
            songElements.add(new SongElement(s) {
                @Override
                public void func() {
                    difficultyChoose = true;
                    buttons.clear();
                    int recomend = game.findMaxAlternative();
                    String text = new String();
                    // добавление кнопок по сложностям
                    if(s.difficultyEasy) {
                        text = "Легкий";
                        if(recomend == 0)
                            text += "(рекомендуем)";
                        buttons.add(new Button(new Rectangle(game.width / 10, game.height / 6 * 3, game.width / 10 * 8, game.height / 6), textureChooseDifficulty, text) {
                            @Override
                            public void func() {
                                int x[] = {100,200,100,200,300,200,100,200,300};
                                int y[] = {100,200,250,300,250,200,250,300,250};
                                int r[] = {80,80,80,80,80,80,80,80,80};
                                int x2[] = {400,500,200,100,50,300,200,400,500};
                                int y2[] = {200,100,550,100,50,400,450,400,150}; ////////////////////////// slider при маленькой дистанции узкий
                                int time2[] = {500,650,800,950,1100,1250,1400,1550,1700};
                                int time[] = {50,100,150,200,250,300,350,400,450};
                                float speed[] = {3,3,3,3,3,3,3,3,3};
                                DataSetCircle dataSetCircle = new DataSetCircle(x,y,r,time,speed);
                                DataSetSlider dataSetSlider = new DataSetSlider(x,y,r,time2,speed,x2,y2);
                                s.dataSetCircle[0] = dataSetCircle;
                                s.dataSetSlider[0] = dataSetSlider;

                                s.loadAll(0);

                                dispose();

                                game.setUserScene(new MainGame(game, s));
                            }
                        });
                    }
                    if(s.difficultyMedium) {
                        text = "Средний";
                        if(recomend == 1)
                            text += "(рекомендуем)";
                        buttons.add(new Button(new Rectangle(game.width / 10, game.height / 6 * 3, game.width / 10 * 8, game.height / 6), textureChooseDifficulty, text) {
                            @Override
                            public void func() {
                                s.loadAll(1);
                                dispose();
                                game.setUserScene(new MainGame(game, s));
                            }
                        });
                    }
                    if(s.difficultyHard) {
                        text = "Сложный";
                        if(recomend == 2)
                            text += "(рекомендуем)";
                        buttons.add(new Button(new Rectangle(game.width / 10, game.height / 6 * 3, game.width / 10 * 8, game.height / 6), textureChooseDifficulty, text) {
                            @Override
                            public void func() {
                                /*int x[] = {100,200,100,200,300,200,100,200,300,100,200,100,200,300,200,100,200,300};
                                int y[] = {100,200,250,300,250,200,250,300,250,100,200,250,300,250,200,250,300,250};
                                int r[] = {80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80};
                                int time[] = {50,110,130,150,170,190,210,260,280,300,320,340,360,450,520,760,770,780};
                                float speed[] = {5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5};
                                DataSetCircle dataSetCircle = new DataSetCircle(x,y,r,time,speed);
                                s.dataSetCircle[2] = dataSetCircle;*/
                                dispose();
                                s.loadAll(2);
                                game.setUserScene(new MainGame(game, s));
                            }
                        });
                    }
                    if(s.difficultyInsane) {
                        text = "Безумный";
                        if(recomend == 3)
                            text += "(рекомендуем)";
                        buttons.add(new Button(new Rectangle(game.width / 10, game.height / 6 * 3, game.width / 10 * 8, game.height / 6), textureChooseDifficulty, text) {
                            @Override
                            public void func() {
                                dispose();
                                s.loadAll(3);
                                game.setUserScene(new MainGame(game, s));
                            }
                        });
                    }

                    buttons.add(buttonBackSongMenu);

                    // упорядочивание расположение кнопок
                    for(int i=0; i<buttons.size; i++){
                        if(buttons.size > 3)
                            buttons.get(i).changeHeight(game.height / 6 * (4 - i));
                        else
                            buttons.get(i).changeHeight(game.height / 6 * (3 - i));
                    }


                }
            });
        setCoordElements();
    }
    void mouseMove(){
        if(Gdx.input.getY() == mouseMove2)
            return;

        if (Gdx.input.isTouched()) {
            if(mouseMove1 == 0)
                mouseMove1 = Gdx.input.getY();
            else{
                mouseMove2 = Gdx.input.getY();
                if(mouseMove2 - mouseMove1 > 20) {
                    mouseMove1 = mouseMove2;
                    moveElements(1);
                }

                if(mouseMove1 - mouseMove2 > 20) {
                    mouseMove1 = mouseMove2;
                    moveElements(-1);
                }
            }
        }
        else {
            mouseMove1 = 0;
            mouseMove2 = 0;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);  // это преобразование координат нажатия в рабочие координаты (координаты камеры)

        if(!difficultyChoose) {
            for (SongElement s : songElements)
                if (s.rect.contains(touchPos.x,touchPos.y))
                    s.func();

            // Кнопки
            buttonMusicPlay.touchDown(touchPos.x,touchPos.y);
            if(downloadMenu)
                buttonCancelDownload.touchDown(touchPos.x,touchPos.y);
            else {
                buttonBackMainMenu.touchDown(touchPos.x,touchPos.y);
                if(game.online)
                    buttonDownloadMenu.touchDown(touchPos.x,touchPos.y);
            }
            if(music != null)
                buttonMusicPlay.touchDown(touchPos.x,touchPos.y);
        }
        else {
            for (Button b:buttons)
                b.touchDown(touchPos.x,touchPos.y);
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    void databaseConnect(){
        dbHandler = DatabaseHandlerFactory.getNewDatabaseHandler(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);
        dbHandler.setupDatabase();
        dbHandler.openOrCreateDatabase();
    }
    void moveElements(int temp) {
        if(songElements.size() != 0) {
            if (songElements.size() != 1) {
                if (temp == 1) {
                    if (songElements.get(0).rect.y == game.height / 2)
                        return;
                    int i = 0;
                    for (SongElement s : songElements) {
                        s.rect.y += game.height / 8;
                        i++;
                    }
                }
                if (temp == -1) {
                    if (songElements.get(songElements.size() - 1).rect.y == game.height / 2)
                        return;
                    int i = 0;
                    for (SongElement s : songElements) {
                        s.rect.y -= game.height / 8;
                        i++;
                    }
                }
            } else {
                songElements.get(0).rect.y = game.height / 2;
                songElements.get(0).select = true;
            }

            int i = 0;
            for (SongElement s : songElements) {
                if (s.rect.y == game.height / 2) {
                    s.rect.x = game.width / 3 * 2 - game.width / 12;
                    s.rect.width = game.width / 3 + game.width / 12;
                    s.select = true;
                    selectSong = s;
                } else {
                    s.rect.x = game.width / 3 * 2;
                    s.rect.width = game.width / 3;
                    s.select = false;
                }
                i++;
            }
        }
    }
    void setCoordElements(){
        int i=4;
        for(SongElement s: songElements)
        {
            s.rect.x = game.width/3*2;
            s.rect.y = game.height/8*i;
            s.rect.width = game.width/3;
            s.rect.height = game.height/8;
            i++;
        }
        if(songElements != null)
            if(songElements.size() != 0)
                music = songElements.get(0).music;
        else
            music = null;
    }


    @Override
    public void show() {
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode)
        {
            case Input.Keys.UP:
                moveElements(1);
                break;
            case Input.Keys.DOWN:
                moveElements(-1);
                break;
        }
        return super.keyDown(keycode);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        mouseMove(); // движение мыши

        // задний фон
        game.batch.begin();
        game.batch.setColor(Color.GRAY);
        if(desktop != null)
            game.batch.draw(desktop,0,0, game.width,game.height);

        game.batch.setColor(Color.WHITE);
        game.batch.draw(textureFrame,0,0,game.width,game.height);
        game.batch.end();

        // Кнопки
        if(downloadMenu)
            buttonCancelDownload.draw(game.batch,game.font);
        else {
            buttonBackMainMenu.draw(game.batch, game.font);
            buttonDelete.draw(game.batch,game.font);
            if(game.online)
                buttonDownloadMenu.draw(game.batch,game.font);
        }
        if(music != null)
            buttonMusicPlay.draw(game.batch,game.font);


        game.batch.begin();
        for(SongElement s: songElements)
        {
            if(s.select){
                game.batch.setColor(Color.WHITE);
                game.batch.draw(s.texture, s.rect.x, s.rect.y, s.rect.width, s.rect.height);
                game.font.draw(game.batch, s.text, s.rect.x, s.rect.y + s.rect.height / 2);
                desktop = s.texture;
            }
            else {
                game.batch.setColor(Color.GRAY);
                game.batch.draw(s.texture, s.rect.x, s.rect.y, s.rect.width, s.rect.height);
                game.font.draw(game.batch, s.text, s.rect.x, s.rect.y + s.rect.height / 2);
            }
        }

        if(!downloadMenu) {
            // Отрисовка персоны
            game.batch.setColor(1, 1, 1, 0.2f);
            if (selectSong != null) {
                game.batch.draw(texturePiece, selectSong.selfScore.rectanglePerson.x, selectSong.selfScore.rectanglePerson.y, selectSong.selfScore.rectanglePerson.width, selectSong.selfScore.rectanglePerson.height);
                game.batch.setColor(Color.WHITE);
                game.font.draw(game.batch, "Ваш рекорд", yourRecordX, yourRecordY);
                game.batch.draw(selectSong.selfScore.avatar, selectSong.selfScore.rectangleAvatar.x, selectSong.selfScore.rectangleAvatar.y, selectSong.selfScore.rectangleAvatar.width, selectSong.selfScore.rectangleAvatar.height);
                if (haveSelfScore) {
                    game.batch.draw(selectSong.selfScore.textureSymbol, selectSong.selfScore.rectangleSymbol.x, selectSong.selfScore.rectangleSymbol.y, selectSong.selfScore.rectangleSymbol.width, selectSong.selfScore.rectangleSymbol.height);
                    game.fontVerySmall.draw(game.batch, "Ник " + selectSong.selfScore.name, selectSong.selfScore.cordNameX, selectSong.selfScore.cordNameY);
                    game.fontVerySmall.draw(game.batch, "Ваша позиция " + selectSong.selfScore.rank + " из " + selectSong.amountPeople, selectSong.selfScore.cordRankX, selectSong.selfScore.cordRankY);
                    game.fontVerySmall.draw(game.batch, "Комбо " + selectSong.selfScore.combo + " из " + selectSong.maxCombo, selectSong.selfScore.cordComboX, selectSong.selfScore.cordComboY);
                    game.fontVerySmall.draw(game.batch, "Точность " + selectSong.selfScore.accuracy + "% из 100.0%", selectSong.selfScore.cordAccuracyX, selectSong.selfScore.cordAccuracyY);
                    game.fontVerySmall.draw(game.batch, "Счет " + selectSong.selfScore.score, selectSong.selfScore.cordScoreX, selectSong.selfScore.cordScoreY);
                } else
                    game.font.draw(game.batch, "Рекорда нет", havntRecordX, havntRecordY);

                for (int i = 0; i < selectSong.topScore.length; i++) {
                    // Отрисовка топ игроков
                    game.batch.setColor(1, 1, 1, 0.2f);
                    game.batch.draw(texturePiece, selectSong.topScore[i].rectanglePerson.x, selectSong.topScore[i].rectanglePerson.y, selectSong.topScore[i].rectanglePerson.width, selectSong.topScore[i].rectanglePerson.height);
                    game.batch.setColor(Color.WHITE);
                    game.batch.draw(selectSong.topScore[i].avatar, selectSong.topScore[i].rectangleAvatar.x, selectSong.topScore[i].rectangleAvatar.y, selectSong.topScore[i].rectangleAvatar.width, selectSong.topScore[i].rectangleAvatar.height);
                    game.batch.draw(selectSong.topScore[i].textureSymbol, selectSong.topScore[i].rectangleSymbol.x, selectSong.topScore[i].rectangleSymbol.y, selectSong.topScore[i].rectangleSymbol.width, selectSong.topScore[i].rectangleSymbol.height);
                    game.fontVerySmall.draw(game.batch, selectSong.topScore[i].name + " Позиция " + selectSong.topScore[i].rank, selectSong.topScore[i].cordNameX, selectSong.topScore[i].cordNameY);
                    game.fontVerySmall.draw(game.batch, "Комбо " + selectSong.topScore[i].combo + " Точность " + selectSong.topScore[i].accuracy + "%", selectSong.topScore[i].cordComboX, selectSong.topScore[i].cordComboY);
                    game.fontVerySmall.draw(game.batch, "Счет " + selectSong.topScore[i].score, selectSong.topScore[i].cordScoreX, selectSong.topScore[i].cordScoreY);
                }
            }
        }
        game.batch.end();

        // отрисовка выбора сложности
        if(difficultyChoose) {
            game.batch.setColor(Color.WHITE);
            for(Button b: buttons){
                b.draw(game.batch,game.font);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.width = width;
        game.height = height;
        camera.setToOrtho(false, game.width, game.height);
        super.resize(width,height);
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
        if(music != null)
            music.stop();
        textureChooseDifficulty.dispose();
        textureBackMainMenu.dispose();
        texturePiece.dispose();
        textureFrame.dispose();
        texturePlay.dispose();
        dbHandler.closeDatabae();
        super.dispose();
    }

    @Override
    public boolean scrolled(int amount) {
        if(!difficultyChoose)
            moveElements(amount);
        return super.scrolled(amount);
    }
}
