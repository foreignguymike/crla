package com.distraction.crla.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.distraction.crla.Constants;
import com.distraction.crla.Context;
import com.distraction.crla.entity.Arm;
import com.distraction.crla.entity.ArmType;
import com.distraction.crla.entity.Instruction;
import com.distraction.crla.entity.LMA;
import com.distraction.crla.entity.Node;
import com.distraction.crla.entity.NodeType;
import com.distraction.crla.entity.pane.InstructionPane;
import com.distraction.crla.entity.pane.LMAPane;
import com.distraction.crla.entity.pane.Pane;

import java.util.ArrayList;
import java.util.List;

public class TrialScreen extends Screen {

    private static final float CAM_PADDING = 0f;
    private static final float ZOOM_STEP = 1.2f;

    private final Rectangle fieldBounds = new Rectangle();
    private final Rectangle scissors = new Rectangle();

    private final LMA lma;
    private final List<Pane> panes;

    private final List<Node> nodes;

    private boolean dragging;
    private final Vector2 pm = new Vector2();
    private final Rectangle panBounds = new Rectangle();


    public TrialScreen(Context context) {
        super(context);

        ignoreInput = true;
        in = new Transition(context, Transition.Type.FLASH_IN, 0.5f, () -> ignoreInput = false);
        in.start();
        out = new Transition(context, Transition.Type.FLASH_OUT, 0.5f);

        List<Arm> arms = new ArrayList<>();
        arms.add(new Arm(context, ArmType.ARM200, Constants.ARM_BLUE));
        arms.add(new Arm(context, ArmType.ARM300, Constants.ARM_GREEN));
        arms.add(new Arm(context, ArmType.ARM200, Constants.ARM_YELLOW));
        nodes = new ArrayList<>();
        nodes.add(new Node(context, NodeType.POSITIVE, 1300, 850));
        nodes.add(new Node(context, NodeType.RES1, 1000, 1000));
        nodes.add(new Node(context, NodeType.POSITIVE, 500, 650));
        lma = new LMA(context, arms, nodes, 1920 / 2f, 1080 / 2f);

        Instruction[][] queue = new Instruction[arms.size()][LMA.MAX_CYCLES];
        for (int arm = 0; arm < arms.size(); arm++) {
            for (int cycle = 0; cycle < queue[0].length; cycle++) {
                queue[arm][cycle] = Instruction.IDLE;
            }
        }
        queue[2][0] = Instruction.CW90;
        queue[1][0] = Instruction.CW90;
        lma.calculate(queue);
        lma.togglePlay();

        panes = new ArrayList<>();
        LMAPane lmaPane = new LMAPane(context, 20, 370, 1400, 600);
        panes.add(new Pane(context, -10, Constants.HEIGHT - 80, 2000, 100));
        panes.add(lmaPane);
//        panes.add(new Pane(context, 20, 270, 1400, 70));
        panes.add(new InstructionPane(context, queue, 20, 20, 1400, 320, arms.size()));
        panes.add(new Pane(context, 1450, 270, 450, 700));
        panes.add(new Pane(context, 1450, 20, 450, 220));

        fieldBounds.set(lmaPane.x - 6, lmaPane.y - 6, lmaPane.w + 12, lmaPane.h + 12);

        float cx = lmaPane.x + lmaPane.w / 2f;
        float cy = lmaPane.y + lmaPane.h / 2f;
        resetCamera();
        calculatePanBounds(cx, cy, lma.getReach());
//        zoomCamera(fieldBounds.x + fieldBounds.width / 2f, fieldBounds.y + fieldBounds.height / 2f, 3f);

        // for scroll
        Gdx.input.setInputProcessor(
            new InputAdapter() {
                @Override
                public boolean scrolled(float amountX, float amountY) {
                    if (ignoreInput) return false;
                    m.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    uiCam.unproject(m);
                    if (!fieldBounds.contains(m.x, m.y)) return false;
                    zoomCamera(m.x, m.y, amountY);
                    return true;
                }
            }
        );
    }

    private void calculatePanBounds(float rootX, float rootY, float reach) {
        float minX = rootX - reach;
        float minY = rootY - reach;
        float maxX = rootX + reach;
        float maxY = rootY + reach;
        for (Node node : nodes) {
            minX = Math.min(minX, node.x - node.w);
            minY = Math.min(minY, node.y - node.w);
            maxX = Math.max(maxX, node.x + node.w);
            maxY = Math.max(maxY, node.y + node.w);
        }
        panBounds.set(
            minX - CAM_PADDING,
            minY - CAM_PADDING,
            maxX - minX + CAM_PADDING * 2,
            maxY - minY + CAM_PADDING * 2
        );
    }

    private void clampCamera() {
        float ox = (fieldBounds.x + fieldBounds.width / 2f - Constants.WIDTH / 2f) * cam.zoom;
        float oy = (fieldBounds.y + fieldBounds.height / 2f - Constants.HEIGHT / 2f) * cam.zoom;
        cam.position.x = MathUtils.clamp(
            cam.position.x,
            panBounds.x - ox,
            panBounds.x + panBounds.width - ox
        );
        cam.position.y = MathUtils.clamp(
            cam.position.y,
            panBounds.y - oy,
            panBounds.y + panBounds.height - oy
        );
    }

    private void handleCameraDrag(float mx, float my) {
        boolean pressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean justPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        if (justPressed && fieldBounds.contains(mx, my)) {
            dragging = true;
            pm.set(mx, my);
        }
        if (!pressed) dragging = false;
        if (dragging) {
            float dx = mx - pm.x;
            float dy = my - pm.y;
            cam.translate(-dx * cam.zoom, -dy * cam.zoom);
            clampCamera();
            cam.update();
            pm.set(mx, my);
        }
    }

    private void zoomCamera(float mx, float my, float amount) {
        if (!fieldBounds.contains(mx, my)) return;
        float oldZoom = cam.zoom;
        float newZoom = MathUtils.clamp(
            oldZoom * (float) Math.pow(ZOOM_STEP, amount),
            1f, 2f
        );
        if (oldZoom == newZoom) return;
        float dx = mx - Constants.WIDTH / 2f;
        float dy = my - Constants.HEIGHT / 2f;
        cam.position.x += dx * (oldZoom - newZoom);
        cam.position.y += dy * (oldZoom - newZoom);
        cam.zoom = newZoom;
        clampCamera();
        cam.update();
    }

    private void resetCamera() {
        cam.zoom = 1f;
        float cx = fieldBounds.x + fieldBounds.width / 2f;
        float cy = fieldBounds.y + fieldBounds.height / 2f;
        cam.position.set(
            Constants.WIDTH - cx,
            Constants.HEIGHT - cy,
            0
        );
        cam.update();
    }

    @Override
    public void input() {
        if (ignoreInput) return;

        m.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        uiCam.unproject(m);

        handleCameraDrag(m.x, m.y);

        for (Pane pane : panes) {
            pane.onMouseMoved(m.x, m.y);
            pane.onMousePressed(Gdx.input.isButtonPressed(Input.Buttons.LEFT));
            pane.onMouseRightPressed(Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
        }
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

        sb.setProjectionMatrix(uiCam.combined);
        sb.setColor(Constants.TRIAL_BG);
        sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);

        renderUi();
        renderLMA();

        sb.setColor(Color.WHITE);
        sb.setProjectionMatrix(uiCam.combined);
        in.render(sb);
        out.render(sb);
        sb.end();
    }

    private void renderLMA() {
        sb.flush();
        ScissorStack.calculateScissors(uiCam, sb.getTransformMatrix(), fieldBounds, scissors);
        if (ScissorStack.pushScissors(scissors)) {
            sb.setProjectionMatrix(cam.combined);
            sb.setColor(Color.WHITE);
            lma.render(sb);
            sb.flush();
            ScissorStack.popScissors();
        }
    }

    private void renderUi() {
        sb.setProjectionMatrix(uiCam.combined);
        for (Pane pane : panes) pane.render(sb);
    }

}
