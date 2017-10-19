package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ReactionTeacher extends Game implements InputProcessor{
	SpriteBatch batch;
	BitmapFont font;
	BitmapFont fontSmall;
	BitmapFont fontVerySmall;
	BitmapFont fontLarge;
	float width;
	float height;
	float[] alternative;
	Texture textureS;
	Texture textureA;
	Texture textureB;
	Texture textureC;
	Texture textureD;
	PersonScore selfScore;
	String login;
	String password;
	String email;
	Texture avatar;
    boolean online = false;
	final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
	String serverIP = "192.168.1.105";
	int port = 7777;
	UserScene userScene;
	int typePlatform;
	public ReactionTeacher(int platform){
		// 0 desktop
		// 1 android
		// 2 html
		// 3 ios
		typePlatform = platform;

		alternative = new float[4];
		alternative[0] = 0.25f;
		alternative[1] = 0.25f;
		alternative[2] = 0.25f;
		alternative[3] = 0.25f;

	}
	public void setUserScene(UserScene us){
		userScene=us;
		this.setScreen(us);
	}
	public void setInput(){
		Gdx.input.setInputProcessor(this);
	}
	public void formulaByes(float [][]matrix, int rank) {
		float[] resultAlternative = new float[alternative.length];
		// начинаем с 1, т.к. 0 - код
		for(int i=0;i<alternative.length;i++)
		{
			float summ = 0;
			// 0 - код, подсчет знаменателя
			for(int z=0; z<matrix[rank].length; z++)
				summ += alternative[z] * matrix[rank][z];
			resultAlternative[i] = alternative[i] * matrix[rank][i] / summ;
		}
		alternative = resultAlternative;
	}
	public void setTextureSymbol(PersonScore s){
		if(s.symbol == 'S')
			s.textureSymbol = textureS;
		if(s.symbol == 'A')
			s.textureSymbol = textureA;
		if(s.symbol == 'B')
			s.textureSymbol = textureB;
		if(s.symbol == 'C')
			s.textureSymbol = textureC;
		if(s.symbol == 'D')
			s.textureSymbol = textureD;
	}
	public int findMaxAlternative(){
		int indexMax = 0;
		float max = 0;
		for(int i=0; i<alternative.length; i++)
			if(max < alternative[i]){
				max = alternative[i];
				indexMax = i;
			}

		return indexMax;
	}
	public void setLogin(String log,String pas,String emai,String avata) {
        login = log;
        password = pas;
		online = true;
		email = emai;
		if(avata != null)
			avatar = new Texture(avata);
		else
			avatar = new Texture("noimage.png");
    }


	@Override
	public void create () {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		// http://bayguzin.ru/main/shriftyi/russkij-shriftyi/sborka-iz-120-i-russkix-shriftov-raznoj-napravlennosti.html
		// шрифт Эрика Байгузина
		//final String FONT_PATH = "Fonty.ttf";
		final String FONT_PATH = "ANTQUAB.TTF";
		textureS = new Texture("S.png");
		textureA = new Texture("A.png");
		textureB = new Texture("B.png");
		textureC = new Texture("C.png");
		textureD = new Texture("D.png");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = FONT_CHARS;
		parameter.size = Gdx.graphics.getHeight() / 18;
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 2;
		font = generator.generateFont(parameter);
		parameter.size /= 1.5;
		fontSmall = generator.generateFont(parameter);
		parameter.size *= 1.5;
		parameter.size /= 2;
		fontVerySmall = generator.generateFont(parameter);
		parameter.size = Gdx.graphics.getHeight() / 12;
		fontLarge = generator.generateFont(parameter);
		generator.dispose();

		selfScore = new PersonScore("StriBog",new Texture("noimage.png"),180000,130,100f,'A',17);

		batch = new SpriteBatch();
		//font = new BitmapFont();
		this.setUserScene(new MainMenuScreen(this));
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		font.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(userScene!=null)userScene.keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//сцены-пееремен
		if(userScene!=null)userScene.touchDown( screenX,  screenY,  pointer,  button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(userScene!=null)
			userScene.touchUp(screenX,screenY,pointer,button);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if(userScene!=null)
			userScene.scrolled(amount);
		return false;
	}
}
