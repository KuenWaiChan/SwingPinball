package Controller.BuildListeners.BoardModifiers;

import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import View.GUIPanel.iBuildGUI;
import physics.Vect;


public class AddGizmoListener extends ABoardModifier {
	protected GizmoType type;

	public AddGizmoListener(iBuildGUI buildGui, IGizmoModel model, GizmoType type) {
		super(buildGui,model);
		this.type = type;
	}


	@Override
	Instruction getInstruction(double x, double y) {
		return getInstruction(x,y,1,1);
	}

	Instruction getInstruction(double x, double y, double w, double h){
		return new DefaultInstructions.AddGizmo(type, (type+ "x"+ (int)x)+"y"+(int)y, new Location2D(x,y,w,h), Vect.ZERO);
	}

@Override
	public String toString(){
		return "AddGizmo[" + type + "]";
}
}
