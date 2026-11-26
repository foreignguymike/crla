package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.crla.Context;

import java.util.List;

public class Machine extends Entity {

    private static final float STEP = 1/60f;

    private final List<Arm> arms;

    private float accum;

    public Machine(Context context, List<Arm> arms) {
        this.arms = arms;
    }

    @Override
    public void update(float dt) {
        accum += dt;
        while (accum >= STEP) {
            updateActual(STEP);
            accum -= STEP;
        }
    }

    private void updateActual(float dt) {
        Arm arm0 = arms.get(0);
        float ex = arm0.ex();
        float ey = arm0.ey();
        float degCum = arm0.deg;
        arm0.update(dt);
        for (int i = 1; i < arms.size(); i++) {
            Arm arm = arms.get(i);
            arm.x = ex;
            arm.y = ey;
            arm.degCum = degCum;
            ex = arm.ex();
            ey = arm.ey();
            degCum += arm.deg;
            arm.update(dt);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        for (Arm arm : arms) {
            arm.render(sb);
        }
    }
}
