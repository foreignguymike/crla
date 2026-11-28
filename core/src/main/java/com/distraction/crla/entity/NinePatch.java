package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Constants;
import com.distraction.crla.utils.Utils;

public class NinePatch extends Entity {

    private final TextureRegion corner;
    private final TextureRegion side;
    private final TextureRegion fill;
    private Color borderColor;
    private Color fillColor;

    public float scale = 1f;

    public NinePatch(TextureRegion corner, TextureRegion side, TextureRegion fill, float x, float y, int w, int h) {
        this(corner, side, fill, Constants.UI1, Constants.UI2, x, y, w, h);
    }

    public NinePatch(TextureRegion corner, TextureRegion side, TextureRegion fill, Color borderColor, Color fillColor, float x, float y, int w, int h) {
        this.corner = corner;
        this.side = side;
        this.fill = fill;
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setBorderColor(Color c) {
        this.borderColor = c;
    }

    public void setFillColor(Color c) {
        this.fillColor = c;
    }

    public void render(SpriteBatch sb) {
        float w = this.w * scale;
        float h = this.h * scale;
        float right = w / 2f + corner.getRegionWidth() / 2f;
        float up = h / 2f + corner.getRegionHeight() / 2f;

        sb.setColor(fillColor);
        Utils.drawCentered(sb, fill, x, y, w + corner.getRegionWidth() * 1.414f, h + corner.getRegionHeight() * 1.414f);

        sb.setColor(borderColor);
        Utils.drawCenteredRotated(sb, corner, x - right, y + up, 0); // tl
        Utils.drawCenteredRotated(sb, corner, x - right, y - up, 90); // bl
        Utils.drawCenteredRotated(sb, corner, x + right, y - up, 180); // br
        Utils.drawCenteredRotated(sb, corner, x + right, y + up, -90); // tr

        Utils.drawCenteredRotated(sb, side, x, y + up, w, side.getRegionHeight(), 0); // up
        Utils.drawCenteredRotated(sb, side, x, y - up, w, side.getRegionHeight(), 180); // down
        Utils.drawCenteredRotated(sb, side, x - right, y, h, side.getRegionHeight(), 90);   // left
        Utils.drawCenteredRotated(sb, side, x + right, y, h, side.getRegionHeight(), -90);  // right
    }
}
