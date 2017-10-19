package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.sqlite.DatabaseCursor;
import com.badlogic.gdx.sqlite.DatabaseHandler;
import com.badlogic.gdx.sqlite.DatabaseHandlerFactory;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by StriBog on 2/24/2017.
 */

public class Song {
    static int maxAmountDiffuclty = 4;
    int id;
    int maxCombo;
    int amountPeople;
    String name;
    String author;
    Music music;
    Texture hitCircle;
    Texture hitCircleOverlay;
    Texture approachCircle;
    Texture background;
    Texture hit0;
    Texture hit50;
    Texture hit100;
    Texture hit300;
    Array<Texture> number;
    Array<PointHit> pointHits;
    boolean difficultyInsane;
    boolean difficultyHard;
    boolean difficultyMedium;
    boolean difficultyEasy;
    DataSetCircle[] dataSetCircle = new DataSetCircle[maxAmountDiffuclty];
    DataSetSlider[] dataSetSlider = new DataSetSlider[maxAmountDiffuclty];
    PersonScore []topScore;
    PersonScore selfScore;


    Song(String Name, Texture Screen, boolean Easy, boolean Medium, boolean Hard, boolean Insane, PersonScore SelfScore, PersonScore []TopScore,int MaxCombo,int AmountPeople){
        name = Name;
        background = Screen;
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/"+name+".mp3"));
        difficultyEasy = Easy;
        difficultyMedium = Medium;
        difficultyHard = Hard;
        difficultyInsane = Insane;
        selfScore = SelfScore;
        topScore = TopScore;
        maxCombo = MaxCombo;
        amountPeople = AmountPeople;
    }
    Song(int ID,String NameSong,String Author,boolean Easy, boolean Medium, boolean Hard, boolean Insane,String Music,String Background,PersonScore SelfScore, PersonScore []TopScore){
        id = ID;
        author = Author;
        name = NameSong;
        if(Background != null)
            background = new Texture(Background);
        music = Gdx.audio.newMusic(Gdx.files.internal(Music));
        difficultyEasy = Easy;
        difficultyMedium = Medium;
        difficultyHard = Hard;
        difficultyInsane = Insane;
        selfScore = SelfScore;
        topScore = TopScore;

        //maxCombo = MaxCombo;
        amountPeople = 50;
    }
    Song(int ID,String NameSong,String Author,boolean Easy, boolean Medium, boolean Hard, boolean Insane,String Music,String Background){
        id = ID;
        author = Author;
        name = NameSong;
        if(Background != null)
            background = new Texture(Background);
        music = Gdx.audio.newMusic(Gdx.files.internal(Music));
        difficultyEasy = Easy;
        difficultyMedium = Medium;
        difficultyHard = Hard;
        difficultyInsane = Insane;
        //maxCombo = MaxCombo;
        //amountPeople = AmountPeople;
    }
    void loadAll(int selectedComplexity){
        hit0 = new Texture("hit0.png");
        hit50 = new Texture("hit50.png");
        hit100 = new Texture("hit100.png");
        hit300 = new Texture("hit300.png");
        number = new Array<Texture>();
        number.add(new Texture("default-1.png"));
        number.add(new Texture("default-2.png"));
        number.add(new Texture("default-3.png"));
        number.add(new Texture("default-4.png"));
        number.add(new Texture("default-5.png"));
        number.add(new Texture("default-6.png"));
        number.add(new Texture("default-7.png"));
        number.add(new Texture("default-8.png"));
        number.add(new Texture("default-9.png"));
        hitCircle = new Texture("hitcircle.png");
        hitCircleOverlay = new Texture("hitcircleoverlay.png");
        approachCircle = new Texture("approachcircle.png");

        pointHits = new Array<PointHit>();

        loadCircles(selectedComplexity);

        // Добавление всех точек
        pointHits.clear();
            if (dataSetCircle[selectedComplexity] != null)
                for (int i = 0; i < dataSetCircle[selectedComplexity].x.length; i++)
                    pointHits.add(new PointCircle(dataSetCircle[selectedComplexity].x[i], dataSetCircle[selectedComplexity].y[i], dataSetCircle[selectedComplexity].r[i], dataSetCircle[selectedComplexity].time[i], dataSetCircle[selectedComplexity].speed[i]));
            if (dataSetSlider[selectedComplexity] != null)
                for (int i = 0; i < dataSetSlider[selectedComplexity].x.length; i++)
                    pointHits.add(new PointSlider(dataSetSlider[selectedComplexity].x[i], dataSetSlider[selectedComplexity].y[i], dataSetSlider[selectedComplexity].r[i], dataSetSlider[selectedComplexity].time[i], dataSetSlider[selectedComplexity].speed[i], dataSetSlider[selectedComplexity].x2[i], dataSetSlider[selectedComplexity].y2[i]));
    }
    void loadCircles(int Complexity){

        String TABLE_PLAYERS = "Players";
        String TABLE_SONGS = "Songs";
        String TABLE_CIRCLE = "Circle";
        String COLUMN_ID = "ID";
        String COLUMN_COMMENT = "Login";
        String DATABASE_NAME = "database.db";
        int DATABASE_VERSION = 1;
        DatabaseHandler dbHandler;

        dbHandler = DatabaseHandlerFactory.getNewDatabaseHandler(DATABASE_NAME, DATABASE_VERSION, null, null);
        dbHandler.setupDatabase();
        dbHandler.openOrCreateDatabase();

        DatabaseCursor cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_CIRCLE + " WHERE Circle.IDSong = + "+id+" AND Circle.Complexity = "+Complexity+";");
        //cursor.getCount();
        ArrayList<Integer> arrayX = new ArrayList<Integer>();
        ArrayList<Integer> arrayY = new ArrayList<Integer>();
        ArrayList<Integer> arrayTime = new ArrayList<Integer>();
        ArrayList<Integer> arrayRadius = new ArrayList<Integer>();
        ArrayList<Float> arraySpeed = new ArrayList<Float>();

        boolean b = cursor.next();
        while (b) {
            arrayX.add(cursor.getInt(3));
            arrayY.add(cursor.getInt(4));
            arrayTime.add(cursor.getInt(5));
            arraySpeed.add((float)cursor.getDouble(6));
            arrayRadius.add(cursor.getInt(7));
            b = cursor.next();
        }
        dbHandler.closeDatabae();

        int []tempX = new int[arrayX.size()];
        int []tempY = new int[arrayY.size()];
        int []tempT = new int[arrayY.size()];
        int []tempR = new int[arrayY.size()];
        float []tempS = new float[arrayY.size()];
        for(int i=0; i<arrayX.size(); i++){
            tempX[i] = arrayX.get(i);
            tempY[i] = arrayY.get(i);
            tempR[i] = arrayRadius.get(i);
            tempT[i] = arrayTime.get(i);
            tempS[i] = arraySpeed.get(i);
        }

        if(Complexity !=0) dataSetCircle[Complexity] = new DataSetCircle(tempX,tempY,tempR,tempT,tempS);

    }
}
