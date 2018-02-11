package de.adesso.brainysnake.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.adesso.brainysnake.BrainySnake;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DesktopLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopLauncher.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        loadConfigurationAnsStartApp();
    }

    private static void loadConfigurationAnsStartApp(){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "BrainySnake";
        config.fullscreen = false;
        config.vSyncEnabled = false;
        config.backgroundFPS = 60;
        config.foregroundFPS = 60;
        config.samples = 4;
        config.width = Config.APPLICATION_WIDTH;
        config.height = Config.APPLICATION_HEIGHT;
        config.addIcon("./core/src/img/icon.png", Files.FileType.Internal);

        new LwjglApplication(new BrainySnake(), config);
        Gdx.app.setLogLevel(Application.LOG_INFO);
        LOGGER.info("DesktopLauncher started");
    }
}
