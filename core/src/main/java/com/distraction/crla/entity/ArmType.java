package com.distraction.crla.entity;

public enum ArmType {
    ARM100("arm100", 195f, 45f);

    final String file;
    final float w;
    final float h;
    ArmType(String file, float w, float h) {
        this.file = file;
        this.w = w;
        this.h = h;
    }
}
