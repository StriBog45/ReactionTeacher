package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.IOException;
import java.net.Socket;

import com.badlogic.gdx.sqlite.DatabaseCursor;
import com.badlogic.gdx.sqlite.DatabaseHandler;
import com.badlogic.gdx.sqlite.DatabaseHandlerFactory;

import lipermi.exception.LipeRMIException;
import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;
import lipermi.net.Server;

public class RemissServer extends ApplicationAdapter implements LipeInterface { //implements IRemoteServer{
    public static final String BINDING_NAME = "remiss";
    Stage stage;
    Label label;
    StringBuilder stringBuilder;
    //Registrar registrar;
    Array<SerializablePlayer> players;
    DatabaseHandler dbHandler;
    TextField loginTextField;
    TextField passTextField;
    NinePatchDrawable ninePatchTextBox;
    NinePatchDrawable ninePatchTextCursor;
    NinePatchDrawable ninePatchBackGroundButton;
    public static final String TABLE_PLAYERS = "Players";
    public static final String TABLE_SONGS = "Songs";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_COMMENT = "Login";
    public static final int PORT = 7777;
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_PLAYERS + "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_COMMENT + " text not null);";

    public RemissServer() {
    }

    ;

    void lipeRMI() {
        stageInit();
        try {
            CallHandler callHandler = new CallHandler();
            callHandler.registerGlobal(LipeInterface.class, this);
            Server server = new Server();
            server.bind(PORT, callHandler);
            server.addServerListener(new IServerListener() {

                @Override
                public void clientDisconnected(Socket socket) {
                    System.out.println("Client Disconnected: " + socket.getInetAddress());
                }

                @Override
                public void clientConnected(Socket socket) {
                    System.out.println("Client Connected: " + socket.getInetAddress());
                }
            });
            System.out.println("Server Listening");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LipeRMIException e) {
            e.printStackTrace();
        }

        dbHandler = DatabaseHandlerFactory.getNewDatabaseHandler(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);
        dbHandler.setupDatabase();
        dbHandler.openOrCreateDatabase();
        dbHandler.execSQL(DATABASE_CREATE);

        DatabaseCursor cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_PLAYERS);
        players = new Array<SerializablePlayer>();

        boolean b = cursor.next();
        while (b) {
            //addText(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            players.add(new SerializablePlayer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            b = cursor.next();
        }
    }

    /*
    void javaRMI(){
        try {
            final Registry registry = LocateRegistry.createRegistry(11701);
            Remote stub = UnicastRemoteObject.exportObject(this, 0);
            registry.bind(BINDING_NAME, stub);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stageInit();
        registrar= new Registrar();

        dbHandler = DatabaseHandlerFactory.getNewDatabaseHandler(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);
        dbHandler.setupDatabase();
        dbHandler.openOrCreateDatabase();
        dbHandler.execSQL(DATABASE_CREATE);

        DatabaseCursor cursor;
        cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_PLAYERS);


        boolean b = cursor.next();
        while (b) {
            addText(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            registrar.register(cursor.getString(1),cursor.getString(2));
            b = cursor.next();
        }
    }*/
    boolean isRegistered(String log) {
        for (SerializablePlayer sp : players) {
            if (sp.login.equals(log))
                return true;
        }
        return false;
    }

    boolean correctPassword(String log, String pas) {
        for (SerializablePlayer sp : players) {
            if (sp.login.equals(log) && sp.password.equals(pas))
                return true;
        }
        return false;
    }

    void getCommand(String command) {
        String[] s = command.split(" ");
        if (s.length != 0) {
            if (s[0].equals("Delete")) {
                addText(s[1] + "was deleted");
            }
            if (s[0].equals("stopLog"))
                addText("Journal suspended");
            if (s[0].equals("getSongs")) {
                DatabaseCursor cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_SONGS);
                players = new Array<SerializablePlayer>();


                boolean b = cursor.next();
                while (b) {
                    addText(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                    b = cursor.next();
                }
            }
        }
    }

    @Override
    public void create() {
        lipeRMI();

    }

    @Override
    public SerializableRecord[] tryGetRecords(int idSongs, int[] places, int selfId) {
        return new SerializableRecord[0];
    }

    @Override
    public SerializableSong[] tryGetSongs() {
        int z = 0;
        DatabaseCursor cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_SONGS);
        boolean b = cursor.next();
        while (b) {
            z++;
            b = cursor.next();
        }


        cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_SONGS);
        players = new Array<SerializablePlayer>();

        SerializableSong songs[] = new SerializableSong[4];
        int i=0;
        b = cursor.next();
        while (b) {
            songs[i] = new SerializableSong(cursor.getInt(0), cursor.getString(1), cursor.getString(2),Boolean.valueOf(cursor.getString(3)), Boolean.valueOf(cursor.getString(4)), Boolean.valueOf(cursor.getString(5)),Boolean.valueOf(cursor.getString(6)),cursor.getString(7),cursor.getString(8));
            b = cursor.next();
            i++;
        }
        addText("request song list");
        return songs;
    }

    public void stageInit() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Table table = new Table();
        table.setFillParent(true);
        stringBuilder = new StringBuilder();
        label = new Label("", labelStyle);
        table.add(label).width(Gdx.graphics.getWidth() * 0.9f);
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFillParent(true);

        ninePatchBackGroundButton = new NinePatchDrawable(new NinePatch(new Texture("backMainMenu.png"), 1, 1, 1, 1));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(ninePatchBackGroundButton, ninePatchBackGroundButton, ninePatchBackGroundButton, new BitmapFont());
        TextButton textButton = new TextButton("", textButtonStyle);

        ninePatchTextBox = new NinePatchDrawable(new NinePatch(new Texture("textBox.png"), 1, 1, 1, 1));
        ninePatchTextCursor = new NinePatchDrawable(new NinePatch(new Texture("textCursor.png"), 1, 1, 1, 1));
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(new BitmapFont(), Color.WHITE, ninePatchTextCursor, null, ninePatchTextBox);
        loginTextField = new TextField("name", textFieldStyle);
        loginTextField.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 8);
        loginTextField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getCommand(loginTextField.getText());
            }
        });

        stage.addActor(scrollPane);
        stage.addActor(loginTextField);

    addText("\n This is output console.");

}

    public void addText(String str) {
        stringBuilder.append(str + "\n");
        label.setText(stringBuilder);
    }

    public void addText(String str, String str1, String str2) {
        stringBuilder.append(str + " " + str1 + " " + str2 + "\n");
        label.setText(stringBuilder);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        dbHandler.closeDatabae();
    }

    // Lipe RMI
    @Override
    public String tryJoin(String login, String pass) {
        if (isRegistered(login)) {
            if (correctPassword(login, pass)) {
                addText("Join successes!Login:" + login + ".");
                return "Успешно";
            } else {
                addText("Join error.Login:" + login + ". Password is not confirmed");
                return "Неверный пароль";
            }
        } else {
            //registrar.register(login,pass);
            //addText("Register successes!Login:"+login+".");
            return "Не существует";
        }
    }

    @Override
    public SerializablePlayer tryGetPlayerInfo(String login) {
        //DatabaseCursor cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE_PLAYERS + " WHERE Login='"+login+"'");
        for (SerializablePlayer sp : players) {
            if (sp.login.equals(login)) {
                addText("Info about:" + sp.login);
                return sp;
            }
        }
        return null;
    }

    @Override
    public String tryRegistration(String login, String pass) {
        if (isRegistered(login)) {
            addText("Busy:" + login);
            return "Занят";
        } else {
            dbHandler.execSQL("INSERT INTO Players (Login,Password) VALUES ('" + login + "','" + pass + "')");
            players.add(new SerializablePlayer(0, login, pass, null, null));
            addText("Register:" + login);
            return "Успешно";
        }
    }

    @Override
    public SerializableDataSong tryGetDataSongs(int idSong) {
        return null;
    }

    @Override
    public float[] tryGetPossibility(int selfId) {
        return new float[0];
    }
}
