package com.mygdx.game;

import java.io.IOException;

import lipermi.handler.CallHandler;
import lipermi.net.Client;

/**
 * Created by StriBog on 6/18/2017.
 */

public class LipeConnect implements LipeInterface {
    private String serverIP;
    private int port;
    private LipeInterface lipeInterface;
    String status;
    LipeConnect(String ip,int serverPort){
        serverIP = ip;
        port = serverPort;
    }
    boolean lipeRMI() {
        try {
            CallHandler callHandler = new CallHandler();
            Client client = new Client(serverIP, port, callHandler);
            lipeInterface = (LipeInterface) client.getGlobal(LipeInterface.class);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    SerializablePlayer join(String login, String pass){
        if(lipeRMI()) {
            status = lipeInterface.tryJoin(login, pass);
            if (status.toString().equals("Успешно")) {
                return lipeInterface.tryGetPlayerInfo(login);
            }
            return null;
        }
        else {
            status = "Оффлайн";
            return null;
        }
    };
    SerializablePlayer registration(String login, String pass){
        if(lipeRMI()) {
            status = lipeInterface.tryRegistration(login, pass);
            if (status.toString().equals("Успешно")) {
                return lipeInterface.tryGetPlayerInfo(login);
            }
            return null;
        }
        else {
            status = "Оффлайн";
            return null;
        }
    };
    SerializableSong []songs(){
        if(lipeRMI()){
            return lipeInterface.tryGetSongs();
        }
        return null;
    }
    SerializableRecord[] records(int idSongs, int[] places, int selfId){
        if(lipeRMI()){
            return lipeInterface.tryGetRecords(idSongs,places,selfId);
        }
        return null;
    }
    SerializableDataSong dataSong(int idSong){
        if(lipeRMI()){
            return lipeInterface.tryGetDataSongs(idSong);
        }
        return null;
    }

    @Override
    public String tryJoin(String login, String pass) {
        return null;
    }

    @Override
    public String tryRegistration(String login, String pass) {
        return null;
    }

    @Override
    public SerializablePlayer tryGetPlayerInfo(String login) {
        return null;
    }

    @Override
    public SerializableSong[] tryGetSongs() {
        return null;
    }

    @Override
    public SerializableRecord[] tryGetRecords(int idSongs, int[] places, int selfId) {
        return null;
    }
    @Override
    public SerializableDataSong tryGetDataSongs(int idSong) {
        return null;
    }

    @Override
    public float[] tryGetPossibility(int selfId) {
        return null;
    }
}
