package com.distraction.crla;

import com.badlogic.gdx.graphics.Color;

public class Constants {

    private Constants() {
    }

    public static final String TITLE = "Critical Lattice";
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    public static final boolean FULLSCREEN = false;

    public static final Color ARM_BLUE = Color.valueOf("64B5F6");
    public static final Color ARM_GREEN = Color.valueOf("56D6B0");
    public static final Color ARM_YELLOW = Color.valueOf("F2BC68");
    public static final Color ARM_PURPLE = Color.valueOf("B39DDB");
    public static final Color[] ARM_COLORS = new Color[]{
        ARM_BLUE, ARM_GREEN, ARM_YELLOW, ARM_PURPLE
    };

    public static final Color POSITIVE = Color.valueOf("55D9F3");
    public static final Color NEGATIVE = Color.valueOf("C18AF5");
    public static final Color RESONANCE = Color.valueOf("F2CF76");
    public static final Color INACTIVE = Color.valueOf("9CAAB9");

    public static final Color UI1 = Color.valueOf("0C121C");
    public static final Color UI2 = Color.valueOf("131E2B");
    public static final Color UI3 = Color.valueOf("1D2C3C");
    public static final Color UI4 = Color.valueOf("30465A");
    public static final Color UI5 = Color.valueOf("71889E");
    public static final Color UI6 = Color.valueOf("DCE7F2");

    public static final Color TRIAL_BG = UI1;
    public static final Color DIALOG_BORDER = UI3;
    public static final Color DIALOG_FILL = UI2;

    public static final Color[] DEFAULT_BORDER_COLORS = new Color[]{
        Constants.UI4,
        Constants.UI5,
        Constants.UI6
    };
    public static final Color[] DEFAULT_FILL_COLORS = new Color[]{
        Constants.UI1,
        Constants.UI4,
        Constants.UI4
    };

}
