package de.adesso.brainysnake.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.adesso.brainysnake.ApplicationController;
import de.adesso.brainysnake.BrainySnake;
import de.adesso.brainysnake.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesktopLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopLauncher.class.getName());

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "BrainySnake";
        config.fullscreen = false;
        config.vSyncEnabled = false;
        config.backgroundFPS = 60;
        config.foregroundFPS = 60;
        config.samples = 4;
        config.width = Config.APPLICATION_WIDTH;
        config.height = Config.APPLICATION_HEIGHT;
        config.addIcon("icon.png", Files.FileType.Internal);

        new LwjglApplication(new ApplicationController(), config);
        Gdx.app.setLogLevel(Application.LOG_INFO);
        LOGGER.info("DesktopLauncher started");    }
}
