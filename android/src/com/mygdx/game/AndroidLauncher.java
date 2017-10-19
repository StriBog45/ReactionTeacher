package com.mygdx.game;

import android.content.Context;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AndroidLauncher extends AndroidApplication {
	Context mycontext;
	private static String DB_NAME = "database.db";
	private String DB_PATH;
	private void copydatabase() throws IOException {
		//Open your local db as the input stream
		InputStream myinput = mycontext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outfilename = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myoutput = new FileOutputStream(mycontext.getDatabasePath(DB_NAME));

		// transfer byte to inputfile to outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myinput.read(buffer))>0) {
			myoutput.write(buffer,0,length);
		}

		//Close the streams
		myoutput.flush();
		myoutput.close();
		myinput.close();
	}
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		mycontext = getContext();
		DB_PATH = getApplicationContext().getPackageName()+"/databases/";
		try{
			copydatabase();
		}
		catch(IOException e){
		};
		initialize(new ReactionTeacher(1), config);
	}
}
