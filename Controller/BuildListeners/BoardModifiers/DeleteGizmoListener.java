package Controller.BuildListeners.BoardModifiers;

import Model.Gizmo.GizmoProperties.IBoardPlacement;
import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import View.GUIPanel.iBuildGUI;

public class DeleteGizmoListener extends ABoardModifier {

	public DeleteGizmoListener(iBuildGUI buildGui, IGizmoModel model) {
		super(buildGui, model);
	}

	@Override
	Instruction getInstruction(double x, double y) {
		return new DefaultInstructions.RemoveGizmo(model.getName(x,y));
	}
}
