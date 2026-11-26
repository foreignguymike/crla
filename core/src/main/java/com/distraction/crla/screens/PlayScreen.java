package com.distraction.crla.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.entity.Arm;
import com.distraction.crla.entity.Machine;
import com.distraction.crla.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen extends Screen {

    private final Color BG_COLOR = new Color(0.1f, 0.15f, 0.3f, 1f);

    private final Machine machine;

    public PlayScreen(Context context) {
        super(context);

        ignoreInput = true;
        in = new Transition(context, Transition.Type.FLASH_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.FLASH_OUT, 0.5f);

        List<Arm> arms = new ArrayList<>();
        arms.add(new Arm(context));
        arms.get(0).x = 600;
        arms.get(0).y = 600;
        arms.add(new Arm(context));
        arms.add(new Arm(context));
        arms.add(new Arm(context));
        arms.add(new Arm(context));
        machine = new Machine(context, arms);
    }

    @Override
    public void input() {
        if (ignoreInput) return;
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);

        machine.update(dt);
    }

    @Override
    public void render() {
        sb.begin();
        sb.setProjectionMatrix(cam.combined);
        sb.setColor(BG_COLOR);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(Color.WHITE);
        machine.render(sb);

        sb.draw(pixel, 590, 590, 20, 20);

        sb.setColor(Color.WHITE);
        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);
        sb.end();
    }

}
