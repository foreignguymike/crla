package com.distraction.crla.entity;

import com.badlogic.gdx.graphics.Color;
import com.distraction.crla.Constants;

public enum NodeType {
    POSITIVE("plus", Constants.POSITIVE, 40),
    NEGATIVE("minus", Constants.NEGATIVE, 40),
    RES1("res1", Constants.RESONANCE, 70, 0),
    RES2("res2", Constants.RESONANCE, 70, 1),
    RES3("res3", Constants.RESONANCE, 70, 2);

    String file;
    Color color;
    float w;
    int resIndex;

    NodeType(String file, Color color, float w) {
        this(file, color, w, -1);
    }

    NodeType(String file, Color color, float w, int resIndex) {
        this.file = file;
        this.color = color;
        this.w = w;
        this.resIndex = resIndex;
    }

    public boolean isResonance() {
        return resIndex >= 0;
    }
}
