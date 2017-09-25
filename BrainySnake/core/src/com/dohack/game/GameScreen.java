package com.dohack.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dohack.game.textures.TextureFactory;


public class GameScreen extends ScreenAdapter {

    TextureFactory textureFactory = new TextureFactory(64,64);
    Pixmap headBlue = textureFactory.getHead(TextureFactory.COLOR_BLUE);

    private SpriteBatch batch;
    private Texture head;
    private Texture img;

    @Override
    public void show() {
        batch = new SpriteBatch();
        head = new Texture(headBlue);
        //img = new Texture("badlogic.jpg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(head, 200,200);
        batch.end();

/*
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
    }
}
