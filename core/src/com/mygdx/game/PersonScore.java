package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by StriBog on 4/8/2017.
 */

public class PersonScore {
    String name;
    Texture avatar;
    Texture textureSymbol;
    int score;
    int combo;
    int rank;
    float accuracy;
    float cordAccuracyX;
    float cordAccuracyY;
    float cordComboX;
    float cordComboY;
    float cordScoreX;
    float cordScoreY;
    float cordRankX;
    float cordRankY;
    float cordNameX;
    float cordNameY;
    char symbol;
    Rectangle rectangleAvatar;
    Rectangle rectangleSymbol;
    Rectangle rectanglePerson;

    PersonScore(){
        avatar = new Texture("noimage.png");
        name = "noname";
        score = 0;
        combo = 0;
        accuracy = 0.0f;
        rank = 0;
    }
    PersonScore(String Name){
        avatar = new Texture("noimage.png");
        name = Name;
        score = 0;
        combo = 0;
        accuracy = 0.0f;
        rank = 0;
    }
    PersonScore(String Name,Texture Avatar){
        avatar = Avatar;
        name = Name;
        score = 0;
        combo = 0;
        accuracy = 0.0f;
        rank = 0;
    }
    PersonScore(String Name,Texture Avatar,int Score,int Combo,float Accuracy,char Symbol,int Rank) {
        name = Name;
        avatar = Avatar;
        score = Score;
        combo = Combo;
        accuracy = Accuracy;
        symbol = Symbol;
        rank = Rank;
    }
    void setPosition(Rectangle rect){
        rectangleAvatar = new Rectangle(rect.x,rect.y,rect.width/8,rect.height);
        rectangleSymbol = new Rectangle(rect.x+rect.width/8,rect.y,rect.width/6,rect.height);
        rectanglePerson = new Rectangle(rect.x,rect.y,rect.width,rect.height);

        cordNameX = rectangleSymbol.x + rectangleSymbol.width + 2;
        cordNameY = rectangleSymbol.y + rectangleSymbol.height - rectanglePerson.height/12;

        cordRankX = cordNameX;
        cordRankY = cordNameY - rectanglePerson.height/6;

        cordComboX = cordRankX;
        cordComboY = cordRankY - rectanglePerson.height/6;

        cordAccuracyX = cordRankX;
        cordAccuracyY = cordComboY - rectanglePerson.height/6;

        cordScoreX = cordRankX;
        cordScoreY = cordAccuracyY - rectanglePerson.height/6;
    }
    void setPositionColumn(Rectangle rect){
        rectangleAvatar = new Rectangle(rect.x,rect.y,rect.width/5,rect.height);
        rectangleSymbol = new Rectangle(rect.x+rect.width/5,rect.y,rect.width/6,rect.height);
        rectanglePerson = new Rectangle(rect.x,rect.y,rect.width,rect.height);

        cordNameX = rectangleSymbol.x + rectangleSymbol.width + 2;
        cordNameY = rectangleSymbol.y + rectangleSymbol.height - rectanglePerson.height/12;

        cordRankX = cordNameX;
        cordRankY = cordNameY - rectanglePerson.height/6;

        cordComboX = cordRankX;
        cordComboY = cordRankY - rectanglePerson.height/6;

        cordAccuracyX = cordRankX;
        cordAccuracyY = cordComboY - rectanglePerson.height/6;

        cordScoreX = cordRankX;
        cordScoreY = cordAccuracyY - rectanglePerson.height/6;
    }
};