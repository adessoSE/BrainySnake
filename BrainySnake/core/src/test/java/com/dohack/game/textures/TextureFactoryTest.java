package com.dohack.game.textures;


import org.junit.Assert;
import org.junit.Test;

public class TextureFactoryTest {

    @Test
    public void eyes() throws Exception {
        TextureFactory textureFactory = new TextureFactory(16,16);

        //Assert.assertFalse(textureFactory.eyes(8,8,16,16));
        //Assert.assertFalse(textureFactory.eyes(8,4,16,16));
        Assert.assertTrue(textureFactory.eyes(2,2,16,16));

    }
}
