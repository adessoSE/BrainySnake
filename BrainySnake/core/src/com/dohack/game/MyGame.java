package com.dohack.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGame extends Game {

	@Override
	public void create () {
        Gdx.app.log("MyGame", "Starting the Game");

        setScreen(new GameScreen());

    }

	@Override
	public void render () {
        super.render(); //important!
	}
	
	@Override
	public void dispose () {

	}
}
