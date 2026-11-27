package com.distraction.crla.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.entity.Arm;
import com.distraction.crla.entity.ArmType;
import com.distraction.crla.entity.Instruction;
import com.distraction.crla.entity.LMA;
import com.distraction.crla.entity.NinePatch;
import com.distraction.crla.entity.Node;
import com.distraction.crla.entity.NodeType;

import java.util.ArrayList;
import java.util.List;

public class TrialScreen extends Screen {

    private final LMA lma;
    private List<NinePatch> dialogs;

    public TrialScreen(Context context) {
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
        nodes.add(new Node(context, NodeType.POSITIVE, 1300, 850));
        nodes.add(new Node(context, NodeType.POSITIVE, 1000, 1000));
        nodes.add(new Node(context, NodeType.POSITIVE, 500, 650));
        lma = new LMA(context, arms, nodes, 1920 / 2f, 1080 / 2f);

        Instruction[][] queue = new Instruction[arms.size()][5];
        for (int arm = 0; arm < arms.size(); arm++) {
            for (int cycle = 0; cycle < queue[0].length; cycle++) {
                queue[arm][cycle] = Instruction.IDLE;
            }
        }
        queue[0][0] = Instruction.CCW90;
        queue[0][1] = Instruction.CCW90;
        lma.calculate(queue);
        lma.togglePlay();

        dialogs = new ArrayList<>();
        TextureRegion corner = context.getImage("dialogcorner");
        TextureRegion side = context.getImage("dialogside");
        dialogs.add(new NinePatch(corner, side, pixel, Constants.DIALOG_BORDER, Constants.DIALOG_FILL, Constants.WIDTH / 2f, Constants.HEIGHT - 30, 2000, 100));
        dialogs.add(new NinePatch(corner, side, pixel, Constants.DIALOG_BORDER, Constants.DIALOG_FILL, 725, 670, 1400, 600));
        dialogs.add(new NinePatch(corner, side, pixel, Constants.DIALOG_BORDER, Constants.DIALOG_FILL, 725, 305, 1400, 70));
        dialogs.add(new NinePatch(corner, side, pixel, Constants.DIALOG_BORDER, Constants.DIALOG_FILL, 725, 130, 1400, 220));
        dialogs.add(new NinePatch(corner, side, pixel, Constants.DIALOG_BORDER, Constants.DIALOG_FILL, 1677, 620, 445, 700));
        dialogs.add(new NinePatch(corner, side, pixel, Constants.DIALOG_BORDER, Constants.DIALOG_FILL, 1677, 130, 445, 220));
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
        renderUi();

        sb.setColor(Color.WHITE);
        lma.render(sb);

        sb.setColor(Color.WHITE);
        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);
        sb.end();
    }

    private void renderUi() {
        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.TRIAL_BG);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        sb.setColor(Constants.DIALOG_BORDER);
        for (NinePatch dialog : dialogs) {
            dialog.render(sb);
        }
    }

}
