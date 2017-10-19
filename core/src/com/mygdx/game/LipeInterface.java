package com.mygdx.game;

/**
 * Created by StriBog on 5/26/2017.
 */

public interface LipeInterface {
    String tryJoin(String login,String pass); // авторизация
    String tryRegistration(String login,String pass); // регистрация
    SerializablePlayer tryGetPlayerInfo(String login); // информация о игроке
    SerializableSong[] tryGetSongs(); // список мелодий на сервере
    SerializableDataSong tryGetDataSongs(int idSong); // получить все данные мелодии(кроме тех, что давались в tryGetSongs)
    SerializableRecord[] tryGetRecords(int idSongs, int[] places, int selfId); // получение рекордов игроков для выбранной мелодии выбранных мест и свой рекорд
    float[] tryGetPossibility(int selfId); // получение вероятностей игрока

}
