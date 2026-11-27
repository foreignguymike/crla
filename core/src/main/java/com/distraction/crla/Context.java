package com.distraction.crla;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.audio.AudioHandler;
import com.distraction.crla.screens.TrialScreen;
import com.distraction.crla.screens.ScreenManager;

public class Context {

    private static final String ATLAS = "crla.atlas";

    public final AssetManager assets;
    public final AudioHandler audio;

    public ScreenManager sm;
    public SpriteBatch sb;

    private final BitmapFont font;

    public Context() {
        assets = new AssetManager();
        assets.load(ATLAS, TextureAtlas.class);
        assets.finishLoading();

        font = new BitmapFont();
        font.getData().markupEnabled = true;

        for (Texture t : assets.get(ATLAS, TextureAtlas.class).getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        audio = new AudioHandler();

        sb = new SpriteBatch();
        sm = new ScreenManager(new TrialScreen(this));
    }

    public TextureRegion getImage(String key) {
        TextureRegion region = assets.get(ATLAS, TextureAtlas.class).findRegion(key);
        if (region == null) throw new IllegalArgumentException("image " + key + " not found");
        return region;
    }

    public TextureRegion getPixel() {
        return getImage("pixel");
    }

    public BitmapFont getFont() {
        return font;
    }

    public void dispose() {
        sb.dispose();
        audio.dispose();
        assets.dispose();
    }

}
