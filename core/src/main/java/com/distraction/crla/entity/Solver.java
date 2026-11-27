package com.distraction.crla.entity;

import com.badlogic.gdx.math.MathUtils;

import java.util.List;

public class Solver {

    private final float rootx;
    private final float rooty;

    final List<Arm> arms;
    final int armCount;
    final int jointCount;

    final List<Node> nodes;

    final float[] jx;
    final float[] jy;
    final float[] rads;

    final int[] startDegs;
    final int[] deltaDegs;
    final float[] lengths;

    final int[] charges;
    final int[] resonances;
    final long[] overlaps;
    private int charge;
    private int resonance;
    private long previousOverlap;

    public Solver(List<Arm> arms, List<Node> nodes, float rootx, float rooty) {
        this.arms = arms;
        armCount = arms.size();
        jointCount = armCount + 1;

        this.nodes = nodes;
        if (nodes.size() > Long.SIZE) throw new IllegalArgumentException("64 nodes max (" + nodes.size() + ")");

        this.rootx = rootx;
        this.rooty = rooty;

        int samples = LMA.MAX_SAMPLES + 1;
        jx = new float[samples * jointCount];
        jy = new float[samples * jointCount];
        rads = new float[samples * armCount];

        startDegs = new int[armCount];
        deltaDegs = new int[armCount];
        lengths = new float[armCount];

        charges = new int[samples];
        resonances = new int[samples];
        overlaps = new long[samples];
    }

    /**
     * @param queue will always have [armsize][10]
     */
    public void calculate(Instruction[][] queue) {
        charge = 0;
        resonance = 0;
        previousOverlap = 0L;

        for (int i = 0; i < armCount; i++) {
            Arm arm = arms.get(i);
            startDegs[i] = arm.deg;
            lengths[i] = arm.w - arm.h;
        }

        calculateSample(0, 0f);

        int sample = 0;
        for (int cycle = 0; cycle < LMA.MAX_CYCLES; cycle++) {
            for (int i = 0; i < armCount; i++) deltaDegs[i] = getDeltaDeg(queue[i][cycle]);
            for (int s = 1; s <= LMA.SAMPLES_PER_CYCLE; s++) {
                float progress = 1f * s / LMA.SAMPLES_PER_CYCLE;
                sample++;
                calculateSample(sample, progress);
            }
            for (int i = 0; i < armCount; i++) startDegs[i] += deltaDegs[i];
        }
    }

    private void calculateSample(int sample, float progress) {
        float x = 0f;
        float y = 0f;
        float deg = 0f;

        int jointOffset = sample * jointCount;
        int armOffset = sample * armCount;

        for (int i = 0; i < armCount; i++) {
            deg += startDegs[i] + deltaDegs[i] * progress;

            jx[jointOffset + i] = x;
            jy[jointOffset + i] = y;
            rads[armOffset + i] = deg * MathUtils.degRad;

            x += MathUtils.cosDeg(deg) * lengths[i];
            y += MathUtils.sinDeg(deg) * lengths[i];
        }

        jx[jointOffset + armCount] = x;
        jy[jointOffset + armCount] = y;

        calculateCollisions(sample, rootx + x, rooty + y);
    }

    private void calculateCollisions(int sample, float cx, float cy) {
        long currentOverlap = 0;
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (!node.contains(cx, cy)) continue;
            long bit = 1L << i;
            currentOverlap |= bit;
            if ((previousOverlap & bit) != 0) continue;

            if (node.type == NodeType.POSITIVE) charge++;
            else if (node.type == NodeType.NEGATIVE) charge--;
            else if (node.type == NodeType.ALPHA && resonance == 0
                || node.type == NodeType.BETA && resonance == 1
                || node.type == NodeType.GAMMA && resonance == 2) resonance++;
            charge = MathUtils.clamp(charge, -3, 3);
        }
        previousOverlap = currentOverlap;
        charges[sample] = charge;
        resonances[sample] = resonance;
        overlaps[sample] = currentOverlap;
    }

    private int getDeltaDeg(Instruction instruction) {
        if (instruction == Instruction.CW90) return -90;
        if (instruction == Instruction.CCW90) return 90;
        return 0;
    }

    public void log(float t) {
        int i = MathUtils.clamp(
            (int) (t * LMA.SAMPLES_PER_CYCLE),
            0,
            LMA.MAX_SAMPLES
        );
        System.out.println("status:: charge: " + charges[i] + ", res: " + resonances[i] + ", overlap: " + (overlaps[i] != 0));
    }
}
