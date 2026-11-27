package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.utils.Utils;

public class Node extends Entity {

    final NodeType type;
    private final TextureRegion base;
    private final TextureRegion mask;
    private final TextureRegion mark;
    private final TextureRegion check;
    private final float r;

    public Node(Context context, NodeType type, float x, float y) {
        this.type = type;
        base = context.getImage("node");
        mask = context.getImage("nodemask");
        mark = context.getImage(type.file);
        check = context.getImage("check");
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

    public void render(SpriteBatch sb, int resonance) {
        sb.setColor(Color.WHITE);
        Utils.drawCentered(sb, base, x, y, w, h);

        Color c = type.color;
        boolean complete = type.isResonance() && resonance > type.resIndex;
        if (type.isResonance() && resonance != type.resIndex) c = Constants.INACTIVE;

        sb.setColor(c);
        Utils.drawCentered(sb, mask, x, y, w, h);
        Utils.drawCentered(sb, mark, x, y, type.w, type.w);
        sb.setColor(Color.WHITE);
        if (complete) Utils.drawCentered(sb, check, x + r * 0.7f, y + r * 0.7f, 40, 40);
    }
}
