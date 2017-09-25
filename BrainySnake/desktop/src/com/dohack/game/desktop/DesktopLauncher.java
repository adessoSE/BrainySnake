package com.dohack.game.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dohack.game.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.fullscreen = true;
        Graphics.DisplayMode desktopDisplayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();

        System.out.println(desktopDisplayMode.toString());

        // Bei mehreren Bildschirmen problematisch
        //config.height = desktopDisplayMode.height;
        //config.width = desktopDisplayMode.width;

        // TODO
        config.height = 768;
        config.width = 1024;



        new LwjglApplication(new MyGame(), config);
	}
}
