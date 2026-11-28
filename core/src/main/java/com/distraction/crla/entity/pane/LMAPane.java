package com.distraction.crla.entity.pane;

import com.badlogic.gdx.graphics.Color;
import com.distraction.crla.Context;

public class LMAPane extends Pane {

    public LMAPane(Context context, float x, float y, int w, int h) {
        super(context, x, y, w, h);
        patch.setFill(context.getImage("lmagradient"));
        patch.setFillColor(Color.WHITE);
    }

}
