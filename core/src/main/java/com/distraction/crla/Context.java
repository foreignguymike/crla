package com.distraction.crla;

import com.badlogic.gdx.Gdx;
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

    public static final String INTER_22 = "fonts/Inter22.fnt";

    public final AssetManager assets;
    public final AudioHandler audio;

    public ScreenManager sm;
    public SpriteBatch sb;

    private final BitmapFont uiFont;
    private final BitmapFont headerFont;

    public Context() {
        assets = new AssetManager();
        assets.load(ATLAS, TextureAtlas.class);
        assets.load(INTER_22, BitmapFont.class);
        assets.finishLoading();

        uiFont = assets.get(INTER_22, BitmapFont.class);
        BitmapFont.BitmapFontData data = new BitmapFont.BitmapFontData(Gdx.files.internal(INTER_22), uiFont.getData().flipped);
        headerFont = new BitmapFont(data, uiFont.getRegion(), uiFont.usesIntegerPositions());
        addTracking(headerFont, 4);

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

    public BitmapFont getUIFont() {
        return uiFont;
    }

    public BitmapFont getHeaderFont() {
        return headerFont;
    }

    private static void addTracking(BitmapFont font, int spacing) {
        BitmapFont.BitmapFontData data = font.getData();
        for (BitmapFont.Glyph[] page : data.glyphs) {
            if (page == null) continue;
            for (BitmapFont.Glyph glyph : page) {
                if (glyph == null) continue;
                glyph.xadvance += spacing;
            }
        }
        data.spaceXadvance += spacing * data.scaleX;
    }

    public void dispose() {
        sb.dispose();
        audio.dispose();
        assets.dispose();
    }

}
