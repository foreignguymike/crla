package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Context;
import com.distraction.crla.utils.Utils;

public class Arm extends Entity {

    private final TextureRegion arm;
    private final TextureRegion mask;
    private final Color color;

    int deg;

    public Arm(Context context, ArmType type, Color color) {
        if (type == ArmType.ARM100) {
            arm = context.getImage(type.file);
            mask = context.getImage(type.file + "mask");
            this.color = color;
            w = type.w;
            h = type.h;
        } else {
            arm = context.getImage(type.file);
            mask = context.getImage(type.file + "mask");
            this.color = color;
            w = type.w;
            h = type.h;
        }
    }

    @Override
    public void update(float dt) {
    }

    public void render(SpriteBatch sb, float x, float y, float rad) {
        sb.setColor(Color.WHITE);
        Utils.drawRotatedArm(sb, arm, x, y, w, h, rad);
        sb.setColor(color);
        Utils.drawRotatedArm(sb, mask, x, y, w, h, rad);
    }
}
