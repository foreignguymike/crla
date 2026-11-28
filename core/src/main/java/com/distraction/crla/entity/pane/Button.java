package com.distraction.crla.entity.pane;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.entity.Entity;
import com.distraction.crla.entity.NinePatch;
import com.distraction.crla.utils.MouseCallback;

public class Button extends Entity {

    public static class ColorData {
        public Color[] borderColors;
        public Color[] fillColors;

        public ColorData() {
            this(Constants.DEFAULT_BORDER_COLORS, Constants.DEFAULT_FILL_COLORS);
        }

        public ColorData(Color[] borderColors, Color[] fillColors) {
            this.borderColors = borderColors;
            this.fillColors = fillColors;
        }
    }

    protected final MouseCallback callback;

    protected final NinePatch patch;
    protected final ColorData colorData;

    public boolean hovered;
    public boolean previousPressed;
    public boolean pressed;

    public Button(Context context, MouseCallback callback, ColorData colorData, float x, float y, int w, int h) {
        this.callback = callback;
        this.colorData = colorData;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        patch = new NinePatch(
            context.getImage("buttoncorner"),
            context.getImage("buttonside"),
            context.getPixel(),
            x, y, w, h
        );
    }

    protected void updateColors() {
        int index;
        if (hovered) {
            if (pressed) {
                index = 2;
            } else {
                index = 1;
            }
        } else {
            index = 0;
        }
        patch.setBorderColor(colorData.borderColors[index]);
        patch.setFillColor(colorData.fillColors[index]);
    }

    public void onMouseMoved(float mx, float my) {
        hovered = contains(mx, my);
        updateColors();
    }

    public boolean onMousePressed(boolean pressed) {
        boolean isPressed = false;
        if (hovered) {
            if (!this.previousPressed && pressed) {
                isPressed = true;
                callback.callback(true);
            }
        }
        previousPressed = pressed;
        this.pressed = pressed && hovered;
        updateColors();
        return isPressed;
    }

    public boolean onMouseRightPressed(boolean pressed) {
        return false;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        patch.render(sb);
    }

}
