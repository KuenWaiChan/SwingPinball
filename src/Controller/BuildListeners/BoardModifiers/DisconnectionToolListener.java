package Controller.BuildListeners.BoardModifiers;

import Controller.BuildListeners.BoardModifiers.DialogueBoxs.connectionDialogue;
import Model.IGizmoModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisconnectionToolListener implements ActionListener {

    private IGizmoModel model;

    public DisconnectionToolListener(IGizmoModel model)
    {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        new connectionDialogue(model, false);
    }
}
