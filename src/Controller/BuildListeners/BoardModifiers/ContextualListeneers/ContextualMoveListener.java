package Controller.BuildListeners.BoardModifiers.ContextualListeneers;

import Controller.BuildListeners.BoardModifiers.ABoardModifier;
import Controller.BuildListeners.BoardModifiers.instructionListenerFactoria;
import Model.Gizmo.GizmoProperties.IBoardPlacement;
import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import View.GUIPanel.BuildGui;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ContextualMoveListener implements ActionListener, MouseListener {

    private BuildGui buildGui;
    private IGizmoModel model;
    private ABoardModifier oldListener;

    public ContextualMoveListener(BuildGui buildGui, IGizmoModel model) {
        this.buildGui = buildGui;
        this.model = model;
        this.oldListener = buildGui.getListener();
    }

    public double scaleValues(double position, double scale) {
        return Math.floor(position / scale);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //todo not updating front end
        buildGui.setMouseClick(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Pair<Double, Double> pair = buildGui.getContextCoord();

        instructionListenerFactoria.setModel(model);

        double newX = scaleValues(e.getX(), buildGui.getScaleX());
        double newY = scaleValues(e.getY(), buildGui.getScaleY());

        if (model.getName(newX, newY).equals("")) {
            instructionListenerFactoria.moveGizmo(pair.getKey(), pair.getValue(),newX, newY );
        }

        buildGui.setMouseClick(oldListener);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

//    Instruction getInstruction(IBoardPlacement place) {
//        Pair<Double, Double> pair = buildGui.getContextCoord();
//        return new DefaultInstructions.Rotate(model.getName((new Location2D(pair.getKey(), pair.getValue(), 1, 1))));
//    }

}









