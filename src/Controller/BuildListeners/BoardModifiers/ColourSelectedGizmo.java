package Controller.BuildListeners.BoardModifiers;

import Model.Board.ConcreteGizmoModel;
import Model.IGizmoModel;
import Model.Instruction;
import Model.ProtoGizmo;
import View.GUIPanel.iBuildGUI;

import java.awt.*;

public class ColourSelectedGizmo extends ABoardModifier {
    public ColourSelectedGizmo(iBuildGUI buildGui, IGizmoModel model) {
        super(buildGui, model);
    }

    @Override
    Instruction getInstruction(double x, double y) {
        return concreteModel -> {
			ProtoGizmo selected = concreteModel.getGizmo(concreteModel.getName(x, y));
			selected.setColor(Color.black);
		};
    }
}
