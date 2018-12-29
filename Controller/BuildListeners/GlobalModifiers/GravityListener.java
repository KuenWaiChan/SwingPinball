package Controller.BuildListeners.GlobalModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;

public class GravityListener extends AGlobalListener {


	private final double baseGravity = 25;

	public GravityListener(IGizmoModel model) {
		super(model, 20, -20, 10, 10);
	}

	@Override
	Instruction getInstruction(double value) {
		return new DefaultInstructions.SetGravity((value)*baseGravity);
	}


}
