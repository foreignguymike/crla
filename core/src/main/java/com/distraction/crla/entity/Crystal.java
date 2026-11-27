package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Context;
import com.distraction.crla.utils.Utils;

public class Crystal extends Entity {

    private static final Color[] CHARGE_COLORS = {
        Color.valueOf("C18AF5"), // -3
        Color.valueOf("A17BC5"), // -2
        Color.valueOf("796B98"), // -1
        Color.valueOf("9CAAB9"), //  0
        Color.valueOf("638FAD"), // +1
        Color.valueOf("55B9D8"), // +2
        Color.valueOf("55D9F3")  // +3
    };

    private final TextureRegion crystal;
    private final TextureRegion container;
    private final TextureRegion glow;

    public Crystal(Context context) {
        crystal = context.getImage("crystal");
        container = context.getImage("container");
        glow = context.getImage("crystalglow");
    }

    public void render(SpriteBatch sb, float x, float y, int charge, int resonance) {
        Color c = CHARGE_COLORS[charge + 3];
        sb.setColor(c.r, c.g, c.b, Math.abs(charge) / 3f * 0.6f);
        Utils.drawCentered(sb, glow, x, y, 140, 140);

        sb.setColor(Color.WHITE);
        Utils.drawCentered(sb, container, x, y, 100, 100);

        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.setColor(c);
        Utils.drawCentered(sb, crystal, x, y, 80, 80);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
