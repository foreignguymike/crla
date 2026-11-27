package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Context;
import com.distraction.crla.utils.Utils;

public class Node extends Entity {

    final NodeType type;
    private final TextureRegion image;
    private final float r;

    public Node(Context context, NodeType type, float x, float y) {
        this.type = type;
        image = context.getImage(type.file);
        this.x = x;
        this.y = y;
        w = h = 100f;
        r = w / 2f;
    }

    public boolean contains(float px, float py) {
        float dx = px - x;
        float dy = py - y;
        return dx * dx + dy * dy <= r * r;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        Utils.drawCentered(sb, image, x, y, w, h);
    }
}
