package com.platformer.desktop;


import com.platformer.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platformer.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config. = Game.TITLE;

		config.title = Game.TITLE;
		config.height = Game.V_HEIGHT * Game.SCALE;
		config.width = Game.V_WIDTH * Game.SCALE;
		new LwjglApplication(new Game(), config);
	}
}
