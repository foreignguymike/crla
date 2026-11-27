package com.distraction.crla.entity;

import com.badlogic.gdx.math.MathUtils;

import java.util.List;

public class Solver {

    final List<Arm> arms;
    final int armCount;
    final float x;
    final float y;

    final float[] jx;
    final float[] jy;
    final float[] rads;

    final int[] startDegs;
    final int[] deltaDegs;
    final float[] lengths;

    public Solver(List<Arm> arms) {
        this.arms = arms;
        armCount = arms.size();
        x = arms.get(0).x;
        y = arms.get(0).y;

        int maxArmSamples = (LMA.MAX_SAMPLES + 1) * armCount;
        jx = new float[maxArmSamples];
        jy = new float[maxArmSamples];
        rads = new float[maxArmSamples];

        startDegs = new int[armCount];
        deltaDegs = new int[armCount];
        lengths = new float[armCount];
    }

    /**
     * @param queue will always have [armsize][10]
     */
    public void calculate(Instruction[][] queue) {
        for (int i = 0; i < armCount; i++) {
            Arm arm = arms.get(i);
            startDegs[i] = arm.deg;
            lengths[i] = arm.w - arm.h;
        }

        calculateSample(0, 0f, x, y);

        int sample = 0;
        for (int cycle = 0; cycle < LMA.MAX_CYCLES; cycle++) {
            for (int i = 0; i < armCount; i++) deltaDegs[i] = getDeltaDeg(queue[i][cycle]);
            for (int s = 1; s < LMA.SAMPLES_PER_CYCLE; s++) {
                float progress = 1f * s / LMA.SAMPLES_PER_CYCLE;
                sample++;
                calculateSample(sample, progress, x, y);
            }
            for (int i = 0; i < armCount; i++) startDegs[i] += deltaDegs[i];
        }
    }

    private void calculateSample(int sample, float progress, float x, float y) {
        float deg = 0;

        int offset = sample * armCount;

        for (int i = 0; i < armCount; i++) {
            deg += startDegs[i] + deltaDegs[i] * progress;

            int index = offset + i;
            jx[index] = x;
            jy[index] = y;
            rads[index] = deg * MathUtils.degRad;

            x += MathUtils.cosDeg(deg) * lengths[i];
            y += MathUtils.sinDeg(deg) * lengths[i];
        }
    }

    private int getDeltaDeg(Instruction instruction) {
        if (instruction == Instruction.CW90) return -90;
        if (instruction == Instruction.CCW90) return 90;
        return 0;
    }
}
