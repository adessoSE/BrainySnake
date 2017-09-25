package com.dohack.game.textures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class TextureFactory {

    public static final Color COLOR_BLUE = Color.NAVY;

    private int width;
    private int height;

    public TextureFactory(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Pixmap getHead(Color color) {
        Pixmap px = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        return getHeadElement(px, color);
    }

    private Pixmap getElement(Pixmap px, Color color) {
        for (int x = 0; x < px.getWidth(); x++) {
            for (int y = 0; y < px.getHeight(); y++) {
                px.setColor(color);
                px.drawPixel(x,y);
            }
        }
    return px;
    }

    private Pixmap getHeadElement(Pixmap px, Color color) {
        for (int x = 0; x < px.getWidth(); x++) {
            for (int y = 0; y < px.getHeight(); y++) {

                if(eyes(x,y,px.getWidth(), px.getHeight())) {
                    px.setColor(Color.WHITE);
                } else {
                    px.setColor(color);
                }

                px.drawPixel(x,y);
            }
        }
        return px;
    }

    public boolean eyes(int posx, int posy, int with, int height) {

        float x = posx;
        float y =posy;

        float w = with;
        float h = height;

        boolean ll = x >= w / 8f;
        boolean lr = x < w * (3f/8f);
        boolean tt = y >= h / 8f;
        boolean tb = y < h * (3f/8f);
        boolean leftEye = ll && lr && tt && tb;

        boolean rl = x >= w * (5f/8f);
        boolean rr = x < w * (7f/8f);
        boolean rightEye = rl && rr && tt && tb;

        return leftEye || rightEye;
    }

}
