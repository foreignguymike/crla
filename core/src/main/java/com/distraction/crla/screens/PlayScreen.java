package com.distraction.crla.screens;

import com.badlogic.gdx.graphics.Color;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.entity.Arm;
import com.distraction.crla.entity.ArmType;
import com.distraction.crla.entity.Instruction;
import com.distraction.crla.entity.LMA;
import com.distraction.crla.entity.Node;
import com.distraction.crla.entity.NodeType;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen extends Screen {

    private final Color BG_COLOR = new Color(0.1f, 0.15f, 0.3f, 1f);

    private final LMA lma;

    public PlayScreen(Context context) {
        super(context);

        ignoreInput = true;
        in = new Transition(context, Transition.Type.FLASH_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.FLASH_OUT, 0.5f);

        List<Arm> arms = new ArrayList<>();
        arms.add(new Arm(context, ArmType.ARM100, Constants.ARM_BLUE));
        arms.add(new Arm(context, ArmType.ARM100, Constants.ARM_GREEN));
        arms.add(new Arm(context, ArmType.ARM100, Constants.ARM_YELLOW));
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(context, NodeType.POSITIVE, 550, 650));
        lma = new LMA(context, arms, nodes, 1920 / 2f, 1080 / 2f);

        Instruction[][] queue = new Instruction[arms.size()][LMA.MAX_CYCLES];
        for (int arm = 0; arm < arms.size(); arm++) {
            for (int cycle = 0; cycle < LMA.MAX_CYCLES; cycle++) {
                queue[arm][cycle] = Instruction.IDLE;
            }
        }
        queue[0][0] = Instruction.CCW90;
        queue[0][1] = Instruction.CCW90;
        lma.calculate(queue);
        lma.togglePlay();
    }

    @Override
    public void input() {
        if (ignoreInput) return;
    }

    @Override
    public void update(float dt) {
        in.update(dt);
        out.update(dt);

        lma.update(dt);
    }

    @Override
    public void render() {
        sb.begin();
        sb.setProjectionMatrix(cam.combined);
        sb.setColor(BG_COLOR);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(Color.WHITE);
        lma.render(sb);

        sb.setColor(Color.WHITE);
        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);
        sb.end();
    }

}
