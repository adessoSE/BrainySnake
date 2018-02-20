package de.adesso.brainysnake;

import com.badlogic.gdx.Game;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import de.adesso.brainysnake.screenmanagement.ScreenType;

public class ApplicationController extends Game {

    @Override
    public void create() {
        ScreenManager screenManager = ScreenManager.getINSTANCE();
        screenManager.initialize(this);
        screenManager.showScreen(ScreenType.MAIN_MENU);
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
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
