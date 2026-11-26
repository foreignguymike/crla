package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {

    public float x;
    public float y;
    public float w;
    public float h;

    public void update(float dt) {}
    public void render(SpriteBatch sb) {}

}
