package com.distraction.crla.entity.pane;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.entity.Instruction;
import com.distraction.crla.entity.LMA;
import com.distraction.crla.entity.NinePatch;
import com.distraction.crla.utils.Utils;

import java.util.Locale;

public class InstructionPane extends Pane {

    private static final int ARM_GAP = 56;
    private static final int CELL_WIDTH = 120;

    private final TextureRegion pixel;

    private final Instruction[][] queue;
    private final TextureRegion armQueueBar;
    private final int armCount;
    private final NinePatch placeholder;

    private final BitmapFont headerFont;
    private final BitmapFont uiFont;

    private final InstructionButton[][] buttons;

    private final float[] ys;

    private final String[] armNames;
    private final String[] cycleCounts;

    public InstructionPane(Context context, Instruction[][] queue, float x, float y, int w, int h, int armCount) {
        super(context, x, y, w, h);
        this.queue = queue;
        headerFont = context.getHeaderFont();
        uiFont = context.getUIFont();
        armQueueBar = context.getImage("armqueuebar");
        pixel = context.getPixel();

        this.armCount = armCount;
        placeholder = new NinePatch(
            context.getImage("buttoncorner"),
            context.getImage("buttonside"),
            context.getPixel(),
            Constants.UI3,
            Constants.UI2,
            x + 780, 0, 1180, 36
        );

        ys = new float[4];
        for (int i = 0; i < ys.length; i++) ys[i] = y + h - 114 - i * ARM_GAP;

        buttons = new InstructionButton[4][LMA.MAX_CYCLES];
        for (int row = 0; row < buttons.length; row++) {
            boolean enabled = row < queue.length;
            for (int col = 0; col < buttons[0].length; col++) {
                final int r = row;
                final int c = col;
                buttons[row][col] = new InstructionButton(
                    context,
                    row,
                    (left) -> {
                        if (left) buttons[r][c].cycleInstruction();
                        else buttons[r][c].removeInstruction();
                        queue[r][c] = buttons[r][c].instruction;
                    },
                    x + 240 + col * CELL_WIDTH,
                    ys[0] - 5 - row * ARM_GAP,
                    CELL_WIDTH - 20,
                    36
                );
                buttons[row][col].enabled = enabled;
            }
        }

        armNames = new String[4];
        for (int i = 0; i < armNames.length; i++) {
            armNames[i] = "Arm " + (i + 1);
        }
        cycleCounts = new String[buttons[0].length];
        for (int i = 0; i < cycleCounts.length; i++) {
            cycleCounts[i] = String.format(Locale.ROOT, "%02d", i + 1);
        }
    }

    private int getTrimmedLength() {
        for (int col = queue[0].length - 1; col >= 0; col--) {
            for (int arm = 0; arm < queue.length; arm++) {
                if (queue[arm][col] != Instruction.IDLE) return col + 1;
            }
        }
        return 0;
    }

    @Override
    public void onMouseMoved(float mx, float my) {
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                buttons[row][col].onMouseMoved(mx, my);
            }
        }
    }

    @Override
    public boolean onMousePressed(boolean pressed) {
        boolean press = false;
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                press |= buttons[row][col].onMousePressed(pressed);
            }
        }
        return press;
    }

    @Override
    public boolean onMouseRightPressed(boolean pressed) {
        boolean press = false;
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[0].length; col++) {
                press |= buttons[row][col].onMouseRightPressed(pressed);
            }
        }
        return press;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        headerFont.setColor(Constants.UI5);
        headerFont.draw(sb, "INSTRUCTION QUEUE", x + 10, y + h - 10);
        for (int arm = 0; arm < buttons.length; arm++) {
            float ax = x + 20;
            float ay = ys[arm];
            if (arm < armCount) {
                uiFont.setColor(Constants.ARM_COLORS[arm]);
                uiFont.draw(sb, armNames[arm], ax + 30, ay);
                sb.setColor(Constants.ARM_COLORS[arm]);
                Utils.drawCentered(sb, armQueueBar, ax, ay - 8, 5, 30);
                for (int col = 0; col < buttons[0].length; col++) {
                    buttons[arm][col].render(sb);
                }
            } else {
                placeholder.y = ys[arm] - 5;
                placeholder.render(sb);
            }
        }
        int length = getTrimmedLength();
        for (int i = 0; i < cycleCounts.length; i++) {
            if (i >= length) {
                sb.setColor(Constants.UI3);
                uiFont.setColor(Constants.UI3);
            } else {
                sb.setColor(Constants.UI5);
                uiFont.setColor(Constants.UI5);
            }
            uiFont.draw(sb, cycleCounts[i], x + 230 + i * CELL_WIDTH, ys[0] + 60);
            sb.draw(pixel, x + 185 + i * CELL_WIDTH, ys[0] + 30, CELL_WIDTH - 10, 4);
        }
    }
}
