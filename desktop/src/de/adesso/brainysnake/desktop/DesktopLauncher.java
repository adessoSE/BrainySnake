package de.adesso.brainysnake.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.BrainySnake;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "BrainySnake";
		config.fullscreen = false;
		config.vSyncEnabled = false;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.samples = 4;
		config.width = Config.APPLICATION_WIDTH;
		config.height = Config.APPLICATION_HEIGHT;

		new LwjglApplication(new BrainySnake(), config);
	}
}
