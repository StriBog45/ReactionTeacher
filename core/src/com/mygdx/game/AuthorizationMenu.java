package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

import lipermi.handler.CallHandler;
import lipermi.net.Client;

/**
 * Created by StriBog on 4/20/2017.
 */

public class AuthorizationMenu extends UserScene {
    final ReactionTeacher game;
    String login;
    String password;
    String status;
    Texture desktopImage;
    Texture textureBackMainMenu;
    Button buttonBackSongMenu;
    Button buttonJoin;
    Vector3 touchPos;
    OrthographicCamera camera;
    LipeInterface lipeInterface;
    //IRemoteServer service;
    Window loginWindow;
    Label loginLabel;
    Label passLabel;
    Label messageLabel;
    TextField loginTextField;
    TextField passTextField;
    NinePatchDrawable ninePatchBackGroundButton;
    NinePatchDrawable ninePatchTextBox;
    NinePatchDrawable ninePatchTextCursor;
    TextButton textButtonJoin;
    TextButton textButtonRegistrar;
    TextButton textButtonSwitch; // кнопка переключения на регистрацию
    private String serverIP = "192.168.1.105";
    public static int PORT = 7767;

    Stage stage;
    Skin skin;
    Label label;
    StringBuilder stringBuilder;
    LipeConnect lipeConnect;

    AuthorizationMenu(final ReactionTeacher gam) {


        game = gam;
        desktopImage = new Texture("Desktop.jpg");
        login = "noname";
        password = "secret";
        status = "";
        textureBackMainMenu = new Texture("backMainMenu.png");
        lipeConnect = new LipeConnect(game.serverIP,game.port);

        stage = new Stage();
        skin = new Skin();
        Gdx.input.setInputProcessor(stage);
        ninePatchBackGroundButton = new NinePatchDrawable(new NinePatch(textureBackMainMenu, 1, 1, 1, 1));
        ninePatchTextBox = new NinePatchDrawable(new NinePatch(new Texture("textBox.png"), 1, 1, 1, 1));
        ninePatchTextCursor = new NinePatchDrawable(new NinePatch(new Texture("textCursor.png"), 1, 1, 1, 1));

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(game.font, Color.WHITE, ninePatchTextCursor, null, ninePatchTextBox);
        Label.LabelStyle labelStyle = new Label.LabelStyle(game.font, Color.WHITE);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(ninePatchBackGroundButton, ninePatchBackGroundButton, ninePatchBackGroundButton, game.font);
        textButtonStyle.fontColor = Color.WHITE;

        float width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();


        // расположение элементов
        loginWindow = new Window("", new Window.WindowStyle(game.font, Color.WHITE, null)); // окно меню
        loginWindow.setMovable(false);
        loginWindow.setBounds(width / 4, height / 4, width / 2, height / 2);
        loginLabel = new Label("Логин: ", labelStyle);
        loginLabel.setBounds(width * 0.03f, height / 3, width * 0.2f, height * 0.1f);
        loginTextField = new TextField("name", textFieldStyle);
        loginTextField.setBounds(width * 0.27f, height / 3, width * 0.2f, height * 0.1f);
        loginTextField.getStyle().background.setLeftWidth(width * 0.2f /12); // установка границ, где пишется текст
        loginTextField.getStyle().background.setRightWidth(width * 0.2f /12);
        passLabel = new Label("Пароль: ", labelStyle);
        passLabel.setBounds(width * 0.03f, height / 4 - height * 0.04f, width * 0.2f, height * 0.1f);
        passTextField = new TextField("---", textFieldStyle);
        passTextField.setPasswordMode(true);
        passTextField.setFocusTraversal(true);
        passTextField.setPasswordCharacter('*');
        passTextField.setBounds(width * 0.27f, height / 4 - height * 0.04f, width * 0.2f, height * 0.1f);
        messageLabel = new Label("---", labelStyle);
        messageLabel.setBounds(width * 0.03f, height / 4 - height * 0.22f, width * 0.4f, height * 0.1f);
        textButtonJoin = new TextButton("Войти", textButtonStyle);
        textButtonJoin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                SerializablePlayer serializablePlayer = lipeConnect.join(loginTextField.getText(),passTextField.getText());
                if(serializablePlayer != null)
                    game.setLogin(serializablePlayer.login, passTextField.getText(), serializablePlayer.email, serializablePlayer.avatar);
                messageLabel.setText(lipeConnect.status);
            }
        });
        textButtonJoin.setBounds(width * 0.27f, height / 4 - height * 0.22f, width * 0.2f, height * 0.1f);

        textButtonRegistrar = new TextButton("Регистрация", textButtonStyle);
        textButtonRegistrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SerializablePlayer serializablePlayer = lipeConnect.registration(loginTextField.getText(),passTextField.getText());
                if(serializablePlayer != null)
                    game.setLogin(serializablePlayer.login, passTextField.getText(), serializablePlayer.email, serializablePlayer.avatar);
                messageLabel.setText(lipeConnect.status);
            }
        });
        textButtonRegistrar.setBounds(width * 0.27f, height / 4 - height * 0.22f, width * 0.2f, height * 0.1f);

        TextButton textButtonBack = new TextButton("       Назад", textButtonStyle);
        textButtonBack.getLabel().setAlignment(Align.left); // размешение текста слева
        textButtonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.batch.setColor(Color.WHITE);
                game.setUserScene(new MainMenuScreen(game));
            }
        });
        textButtonBack.setBounds(0, 0, game.width / 10 * 3, game.height / 8);

        textButtonJoin.setBounds(width * 0.27f, height / 4 - height * 0.22f, width * 0.2f, height * 0.1f);

        textButtonSwitch = new TextButton("Регистрация    ", textButtonStyle); // кнопка переключения на регистрацию
        textButtonSwitch.getLabel().setAlignment(Align.right); // размешение текста справа
        textButtonSwitch.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loginWindow.removeActor(textButtonJoin);
                loginWindow.addActor(textButtonRegistrar);
                textButtonSwitch.remove();
            }
        });
        textButtonSwitch.setBounds(game.width - game.width / 10 * 3, 0, game.width / 10 * 3, game.height / 8);

        loginWindow.addActor(loginLabel);
        loginWindow.addActor(passLabel);
        loginWindow.addActor(messageLabel);
        loginWindow.addActor(loginTextField);
        loginWindow.addActor(passTextField);
        loginWindow.addActor(textButtonJoin);
        stage.addActor(loginWindow);
        stage.addActor(textButtonBack);
        stage.addActor(textButtonSwitch);

        touchPos = new Vector3();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, gam.width, gam.height);
    }

    boolean lipeRMI() {
        try {
            CallHandler callHandler = new CallHandler();
            Client client = new Client(serverIP, PORT, callHandler);
            lipeInterface = (LipeInterface) client.getGlobal(LipeInterface.class);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean connect() {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);  // это преобразование координат нажатия в рабочие координаты (координаты камеры)

        buttonBackSongMenu.touchDown(touchPos.x, touchPos.y);
        buttonJoin.touchDown(touchPos.x, touchPos.y);

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return super.scrolled(amount);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {

        //shift 59
        char symbol = ' ';
        for (int i = 7; i < 17; i++) {
            if (keycode == i)
                symbol = (char) i;
        }
        for (int i = 29; i < 55; i++) {
            if (keycode == i)
                symbol = (char) i;
        }
        String temp = KeyEvent.getKeyText(keycode);

        //login += keycode + "";
        /*
        switch (keycode)
        {
            case Input.Keys.Q:
                symbol = 'q';
                break;
            case Input.Keys.W:
                symbol = 'w';
                break;
            case Input.Keys.E:
                symbol = 'e';
                break;
            case Input.Keys.R:
                symbol = 'r';
                break;
            case Input.Keys.T:
                symbol = 't';
                break;
            case Input.Keys.Y:
                symbol = 'y';
                break;
            case Input.Keys.U:
                symbol = 'u';
                break;
            case Input.Keys.I:
                symbol = 'i';
                break;
            case Input.Keys.O:
                symbol = 'o';
                break;
            case Input.Keys.P:
                symbol = 'p';
                break;
            case Input.Keys.A:
                symbol = 'a';
                break;
            case Input.Keys.S:
                symbol = 's';
                break;
            case Input.Keys.D:
                symbol = 'd';
                break;
        }*/
        login += symbol;
        return super.keyDown(keycode);
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
        game.batch.draw(desktopImage, 0, 0, game.width, game.height);

        /*
        // кнопки
        game.batch.draw(buttonBackSongMenu.texture,buttonBackSongMenu.rect.x,buttonBackSongMenu.rect.y,buttonBackSongMenu.rect.width,buttonBackSongMenu.rect.height);
        game.font.draw(game.batch,buttonBackSongMenu.text,buttonBackSongMenu.textX,buttonBackSongMenu.textY);
        game.batch.draw(buttonJoin.texture,buttonJoin.rect.x,buttonJoin.rect.y,buttonJoin.rect.width,buttonJoin.rect.height);
        game.font.draw(game.batch,buttonJoin.text,buttonJoin.textX,buttonJoin.textY);

        game.font.draw(game.batch,login,game.width/4,game.height/6*5);
        game.font.draw(game.batch,password,game.width/4,game.height/6*4);
        game.font.draw(game.batch,status,game.width/4,game.height/6*3);*/
        game.batch.end();

        stage.draw();
        stage.act();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
    }
}
