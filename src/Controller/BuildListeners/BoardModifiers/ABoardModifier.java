package Controller.BuildListeners.BoardModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.GizmoType;
import View.GUIPanel.iBuildGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class ABoardModifier implements MouseListener, ActionListener {
    protected iBuildGUI buildGui;
    protected IGizmoModel model;

    public ABoardModifier(iBuildGUI buildGui, IGizmoModel model) {
        this.buildGui = buildGui;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        buildGui.setMouseClick(this);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        double tx = scaleValues(e.getX(), buildGui.getScaleX());
        double ty = scaleValues(e.getY(), buildGui.getScaleY());
        Thread t = new Thread(() -> {
            if (SwingUtilities.isRightMouseButton(e)) {
                callPopup(e, tx, ty);
            } else {
                Instruction i = getInstruction(tx,ty);
                if (i != null) {
                    model.executeInstruction(i);
                }
            }
        });
        t.start();
    }

    public void callPopup(MouseEvent e, double x, double y) {
        GizmoType type = model.getType(x,y);
        String name = model.getName(x,y);
        if(type!=null)
            buildGui.popUpMenu(type, e, x, y, name);

    }

    abstract Instruction getInstruction(double x, double y);

    public double scaleValues(double position, double scale) {
        return Math.floor(position / scale);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
