package Controller.BuildListeners.BoardModifiers.ContextualListeneers;

import Controller.BuildListeners.BoardModifiers.ABoardModifier;
import Controller.BuildListeners.BoardModifiers.IKeyConnectListener;
import Controller.BuildListeners.BoardModifiers.DialogueBoxs.KeyConnectDialog;
import Controller.BuildListeners.BoardModifiers.instructionListenerFactoria;
import Model.IGizmoModel;
import View.GUIPanel.BuildGui;
import javafx.util.Pair;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ContextualKeyConnectListener implements ActionListener, MouseListener, IKeyConnectListener {

    private BuildGui buildGui;
    private IGizmoModel model;
    private ABoardModifier oldListener;
    private Integer keyStroke;
    private String direction;
    private boolean submitted, approved;
    private KeyConnectDialog d;

    public ContextualKeyConnectListener(BuildGui buildGui, IGizmoModel model) {
        this.buildGui = buildGui;
        this.model = model;
        this.oldListener = buildGui.getListener();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Thread t = new Thread(() -> {
            Pair<Double, Double> pair = buildGui.getContextCoord();
        if (openDialogue()) {
            instructionListenerFactoria.setModel(model);
            instructionListenerFactoria.keyConnectGizmo(keyStroke, direction, pair.getKey(), pair.getValue());
        }});
        t.start();
    }

    private boolean openDialogue() {
        d = new KeyConnectDialog(this);
        while (!submitted) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
        d.dispose();
        d = null;
        return approved;
    }

    @Override
    public void cancel() {
        submitted = true;
        approved = false;
    }

    @Override
    public void submitData(Integer keyStroke, String actionCommand) {
        submitted = true;
        approved = true;
        this.keyStroke = keyStroke;
        this.direction = actionCommand;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }



}

