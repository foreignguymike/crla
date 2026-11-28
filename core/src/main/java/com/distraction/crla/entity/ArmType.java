package com.distraction.crla.entity;

public enum ArmType {
    ARM200(200f, 45f),
    ARM300(300f, 45f),
    ;

    final float w;
    final float h;
    ArmType(float w, float h) {
        this.w = w;
        this.h = h;
    }
}
