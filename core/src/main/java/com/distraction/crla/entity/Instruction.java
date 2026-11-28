package com.distraction.crla.entity;

public enum Instruction {
    IDLE(6, 6),
    CW90(36, 36),
    CCW90(36, 36)
    ;
    public final float w;
    public final float h;
    Instruction(float w, float h) {
        this.w = w;
        this.h = h;
    }
}
