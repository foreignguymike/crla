package com.distraction.crla.entity.pane;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.entity.Instruction;
import com.distraction.crla.utils.MouseCallback;
import com.distraction.crla.utils.Utils;

public class InstructionButton extends Button {

    private static final ColorData COLOR_DATA = new ColorData();

    private final TextureRegion cw;
    private final TextureRegion ccw;
    private final TextureRegion idle;

    private final int arm;
    public Instruction instruction;
    private TextureRegion image;

    private boolean rightPressed;
    public boolean enabled = true;

    public InstructionButton(Context context, int arm, MouseCallback callback, float x, float y, int w, int h) {
        super(context, callback, COLOR_DATA, x, y, w, h);
        cw = context.getImage("cw");
        ccw = context.getImage("ccw");
        idle = context.getImage("idle");
        this.arm = arm;
        instruction = Instruction.IDLE;
        updateColors();
        updateImages();
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
        updateImages();
    }

    public void cycleInstruction() {
        this.instruction = Utils.next(instruction);
        updateImages();
    }

    public void removeInstruction() {
        this.instruction = Instruction.IDLE;
        updateImages();
    }

    private void updateImages() {
        if (instruction == Instruction.IDLE) image = idle;
        else if (instruction == Instruction.CW90) image = cw;
        else if (instruction == Instruction.CCW90) image = ccw;
    }

    @Override
    public void onMouseMoved(float mx, float my) {
        if (enabled) super.onMouseMoved(mx, my);
    }

    @Override
    public boolean onMouseRightPressed(boolean pressed) {
        boolean isPressed = false;
        if (pressed) {
            if (hovered) {
                if (!this.rightPressed) {
                    isPressed = true;
                    callback.callback(false);
                }
                this.rightPressed = true;
            }
        } else {
            this.rightPressed = false;
        }
        return isPressed;
    }

    @Override
    protected void updateColors() {
        int index;
        if (hovered) {
            if (pressed) {
                index = 2;
            } else {
                index = 1;
            }
        } else if (instruction != Instruction.IDLE) {
            patch.setBorderColor(Constants.UI4);
            patch.setFillColor(Constants.UI3);
            return;
        } else {
            index = 0;
        }
        patch.setBorderColor(colorData.borderColors[index]);
        patch.setFillColor(colorData.fillColors[index]);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (instruction == Instruction.IDLE) {
            if (hovered) sb.setColor(Constants.UI5);
            else sb.setColor(Constants.UI4);
        } else sb.setColor(Constants.ARM_COLORS[arm]);
        Utils.drawCentered(sb, image, x, y, instruction.w, instruction.h);
    }
}
