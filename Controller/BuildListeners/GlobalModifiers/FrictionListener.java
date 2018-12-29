package Controller.BuildListeners.GlobalModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import View.Styliser;

import javax.swing.*;
import java.util.Hashtable;

public class FrictionListener extends AGlobalListener {
	private double base = 0.025;
	JSlider frictionY;
	public FrictionListener(IGizmoModel model) {
		super(model, 40, 0, 10, 10);
	}

	public JSlider createYSlider(){
		frictionY = genSlider();
		return frictionY;
	}

	@Override
	Instruction getInstruction(double value) {
		return new DefaultInstructions.SetFriction(value * base, (frictionY.getValue()/SCALE) * base);
	}
}
