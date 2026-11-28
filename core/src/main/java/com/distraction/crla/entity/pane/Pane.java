package com.distraction.crla.entity.pane;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.entity.Entity;
import com.distraction.crla.entity.NinePatch;

/**
 * For ease, Panes will be bottom left oriented.
 */
public class Pane extends Entity {

    protected final TextureRegion corner;
    protected final TextureRegion side;
    protected final TextureRegion pixel;
    protected final NinePatch patch;

    public Pane(Context context, float x, float y, int w, int h) {
        corner = context.getImage("dialogcorner");
        side = context.getImage("dialogside");
        pixel = context.getPixel();

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        patch = new NinePatch(corner, side, pixel, Constants.DIALOG_BORDER, Constants.DIALOG_FILL, x + w / 2f, y + h / 2f, w, h);
    }

    public void onMouseMoved(float mx, float my) {
    }

    public boolean onMousePressed(boolean pressed) {
        return false;
    }

    public boolean onMouseRightPressed(boolean pressed) {
        return false;
    }

    public void render(SpriteBatch sb) {
        patch.render(sb);
    }

}
