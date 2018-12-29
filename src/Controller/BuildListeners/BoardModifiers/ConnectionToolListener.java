package Controller.BuildListeners.BoardModifiers;

import Controller.BuildListeners.BoardModifiers.DialogueBoxs.connectionDialogue;
import Model.Gizmo.GizmoProperties.IBoardPlacement;
import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import View.GUIPanel.iBuildGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class ConnectionToolListener implements ActionListener {

    private IGizmoModel model;

    public ConnectionToolListener (IGizmoModel model)
    {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        new connectionDialogue(model, true);
    }
}
