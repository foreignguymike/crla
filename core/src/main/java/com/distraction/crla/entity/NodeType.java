package com.distraction.crla.entity;

public enum NodeType {
    POSITIVE("positivenode"),
    NEGATIVE("negativenode"),
    ALPHA("alpha"),
    BETA("beta"),
    GAMMA("gamma")
    ;

    String file;
    NodeType(String file) {
        this.file = file;
    }
}
