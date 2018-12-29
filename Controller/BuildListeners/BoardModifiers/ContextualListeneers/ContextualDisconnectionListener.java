package Controller.BuildListeners.BoardModifiers.ContextualListeneers;

import Controller.BuildListeners.BoardModifiers.ABoardModifier;
import Controller.BuildListeners.BoardModifiers.DialogueBoxs.connectionDialogue;
import Model.IGizmoModel;
import View.GUIPanel.BuildGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContextualDisconnectionListener implements ActionListener {

    private BuildGui buildGui;
    private IGizmoModel model;
    private ABoardModifier oldListener;

    public ContextualDisconnectionListener(BuildGui buildGui, IGizmoModel model) {
        this.buildGui = buildGui;
        this.model = model;
        this.oldListener = buildGui.getListener();
    }
    public double scaleValues(double position, double scale) {
        return Math.floor(position / scale);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double rcx = buildGui.getRightClickX();
        double rcy = buildGui.getRightClickY();
        new connectionDialogue(model, model.getName(rcx, rcy), false);
    }

}