package Controller.BuildListeners.BoardModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import View.GUIPanel.iBuildGUI;

public class RotateGizmoListener extends ABoardModifier {

    public RotateGizmoListener(iBuildGUI buildGui, IGizmoModel model) {
        super(buildGui, model);
    }

    @Override
    Instruction getInstruction(double x , double y) {

        if (!model.getName(x,y) .equals( ""))
            return new DefaultInstructions.Rotate(model.getName(x,y ));
        return null;
    }
}
