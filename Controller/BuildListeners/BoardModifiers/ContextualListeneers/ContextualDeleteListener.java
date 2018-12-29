package Controller.BuildListeners.BoardModifiers.ContextualListeneers;

import Controller.BuildListeners.BoardModifiers.instructionListenerFactoria;
import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.util.DefaultInstructions;
import View.GUIPanel.BuildGui;
import javafx.util.Pair;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContextualDeleteListener implements ActionListener {

    private BuildGui buildGui;
    private IGizmoModel model;

    public ContextualDeleteListener(BuildGui buildGui, IGizmoModel model) {
        this.buildGui = buildGui;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Pair<Double, Double> pair = buildGui.getContextCoord();

        instructionListenerFactoria.setModel(model);
        instructionListenerFactoria.deleteGizmo(pair.getKey(), pair.getValue());
    }
}