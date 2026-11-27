package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.crla.Context;
import com.distraction.crla.utils.Utils;

import java.util.List;

/**
 * Lattice Manipulation Apparatus
 */
public class LMA extends Entity {

    public static final int MAX_CYCLES = 10;
    public static final int SAMPLES_PER_CYCLE = 180;
    public static final int MAX_SAMPLES = MAX_CYCLES * SAMPLES_PER_CYCLE;

    private final TextureRegion root;

    private final List<Arm> arms;
    private final Solver solver;
    private final float x;
    private final float y;

    private int speed = 1;
    private float t;
    private boolean playing;

    public LMA(Context context, List<Arm> arms, float x, float y) {
        root = context.getImage("root");

        this.arms = arms;
        this.x = x;
        this.y = y;
        solver = new Solver(arms);
    }

    public void calculate(Instruction[][] queue) {
        solver.calculate(queue);
    }

    public void togglePlay() {
        playing = !playing;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void jumpTo(float t) {
        this.t = t;
        playing = false;
    }

    @Override
    public void update(float dt) {
        if (playing) {
            t += dt * speed;
            if (t > MAX_CYCLES) {
                t = MAX_CYCLES;
                playing = false;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        Utils.drawCentered(sb, root, x, y, 100, 100);
        float sample = MathUtils.clamp(
            t * SAMPLES_PER_CYCLE,
            0,
            MAX_SAMPLES - 1
        );
        int a = (int) sample;
        int b = Math.min(a + 1, MAX_SAMPLES - 1);
        float alpha = sample - a;
        int armCount = arms.size();
        int offseta = a * armCount;
        int offsetb = b * armCount;
        for (int i = 0; i < armCount; i++) {
            int ia = offseta + i;
            int ib = offsetb + i;
            float jx = MathUtils.lerp(solver.jx[ia], solver.jx[ib], alpha);
            float jy = MathUtils.lerp(solver.jy[ia], solver.jy[ib], alpha);
            float rad = MathUtils.lerp(solver.rads[ia], solver.rads[ib], alpha);
            arms.get(i).render(sb, x + jx, y + jy, rad);
        }
    }
}
