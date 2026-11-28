package com.distraction.crla.entity;

public abstract class Entity {
    public float x;
    public float y;
    public float w;
    public float h;

    public boolean contains(float px, float py) {
        float w2 = w / 2f;
        float h2 = h / 2f;
        return px >= x - w2 &&
            px <= x + w2 &&
            py >= y - h2 &&
            py <= y + h2;
    }
}
