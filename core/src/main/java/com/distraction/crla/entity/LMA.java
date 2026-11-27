package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.crla.Constants;
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
    private final List<Node> nodes;
    private final Solver solver;
    private final float x;
    private final float y;

    private final Crystal crystal;

    private int speed = 1;
    private float t;
    private boolean playing;

    public LMA(Context context, List<Arm> arms, List<Node> nodes, float x, float y) {
        root = context.getImage("root");

        this.arms = arms;
        this.nodes = nodes;
        this.x = x;
        this.y = y;
        solver = new Solver(arms, nodes, x, y);

        crystal = new Crystal(context);
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

    public void update(float dt) {
        if (playing) {
            t += dt * speed;
            if (t > MAX_CYCLES) {
                t = MAX_CYCLES;
                playing = false;
            }
        }
    }

    public void render(SpriteBatch sb) {
        int sample = MathUtils.clamp(
            (int) (t * SAMPLES_PER_CYCLE),
            0,
            MAX_SAMPLES - 1
        );
        renderNodes(sb, sample);
        renderArms(sb);
    }

    private void renderNodes(SpriteBatch sb, int sample) {
        int resonance = solver.resonances[sample];
        for (Node node : nodes) {
            node.render(sb, resonance);
        }
    }

    private void renderArms(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        Utils.drawCentered(sb, root, x, y, 100, 100);
        float sample = MathUtils.clamp(
            t * SAMPLES_PER_CYCLE,
            0,
            MAX_SAMPLES - 1
        );
        int a = (int) sample;
        int b = Math.min(a + 1, MAX_SAMPLES - 1);
        int ja = a * solver.jointCount;
        int jb = b * solver.jointCount;
        int ra = a * solver.armCount;
        int rb = b * solver.armCount;
        float alpha = sample - a;
        int armCount = arms.size();
        for (int i = 0; i < armCount; i++) {
            float jx = MathUtils.lerp(solver.jx[ja + i], solver.jx[jb + i], alpha);
            float jy = MathUtils.lerp(solver.jy[ja + i], solver.jy[jb + i], alpha);
            float rad = MathUtils.lerp(solver.rads[ra + i], solver.rads[rb + i], alpha);
            arms.get(i).render(sb, x + jx, y + jy, rad);
        }
        float cx = MathUtils.lerp(
            solver.jx[ja + armCount],
            solver.jx[jb + armCount],
            alpha
        );
        float cy = MathUtils.lerp(
            solver.jy[ja + armCount],
            solver.jy[jb + armCount],
            alpha
        );
        crystal.render(sb, x + cx, y + cy, solver.charges[a], solver.resonances[a]);
    }
}
