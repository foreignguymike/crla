package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.crla.Context;
import com.distraction.crla.utils.Utils;

public class Arm extends Entity {

    private final TextureRegion arm;
    float deg;
    float degCum;
    private float degr;

    public Arm(Context context) {
        arm = context.getImage("arm");
        w = arm.getRegionWidth();
        h = arm.getRegionHeight();

        degr = MathUtils.random() < 0.5f ? 40 : 40;
    }

    public float ex() {
        return x + MathUtils.cos((deg + degCum) * MathUtils.degRad) * (w - h);
    }

    public float ey() {
        return y + MathUtils.sin((deg + degCum) * MathUtils.degRad) * (w - h);
    }

    @Override
    public void update(float dt) {
        deg += degr * dt;
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.drawRotatedArm(sb, arm, x, y, (deg + degCum));
    }
}
