package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Context;
import com.distraction.crla.utils.Utils;

public class Arm extends Entity {

    private final TextureRegion armBar;
    private final TextureRegion armBarMask;
    private final TextureRegion armJoint;
    private final TextureRegion armJointMask;
    private final Color color;

    int deg;

    public Arm(Context context, ArmType type, Color color) {
        armBar = context.getImage("armbar");
        armBarMask = context.getImage("armbarmask");
        armJoint = context.getImage("armjoint");
        armJointMask = context.getImage("armjointmask");
        this.color = color;
        if (type == ArmType.ARM200) {
            w = type.w;
            h = type.h;
        } else {
            w = type.w;
            h = type.h;
        }
    }

    public void update(float dt) {
    }

    public void render(SpriteBatch sb, float x, float y, float rad) {
        sb.setColor(Color.WHITE);
        Utils.drawRotatedArm(sb, armBar, x, y, w, h, rad);
        sb.setColor(color);
        Utils.drawRotatedArm(sb, armBarMask, x, y, w, h, rad);
        sb.setColor(1, 1, 1, 1);
        Utils.drawCentered(sb, armJoint, x, y, h, h);
    }
}
