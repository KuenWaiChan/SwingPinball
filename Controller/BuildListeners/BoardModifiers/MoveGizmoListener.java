package Controller.BuildListeners.BoardModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import View.GUIPanel.iBuildGUI;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MoveGizmoListener extends ABoardModifier {
	public MoveGizmoListener(iBuildGUI buildGui, IGizmoModel model) {
		super(buildGui, model);
		name = "";
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		double x = scaleValues(e.getX(), buildGui.getScaleX());
		double y = scaleValues(e.getY(), buildGui.getScaleY());
		if (type == GizmoType.Ball) {
			x = scaleAccurateValues(e.getX(), buildGui.getScaleX());
			y = scaleAccurateValues(e.getY(), buildGui.getScaleY());
		}
		double tx = x, ty = y;
		Thread t = new Thread(() -> {
			if (SwingUtilities.isRightMouseButton(e))
				callPopup(e, tx, ty);
			else {
				Instruction i = getInstruction(tx, ty);
				if (i != null)
					model.executeInstruction(i);
			}
		});
		t.start();
	}

	private String name;
	private GizmoType type;

	@Override
	Instruction getInstruction(double x, double y) {
		if (model.getName(x, y).equals("")) {
			if (!name.equals("")) {
				Instruction i = new DefaultInstructions.Shift(x, y, name);
				name = "";
				type = null;
				return i;
			}
		} else {
			name = model.getName(x, y);
			type = model.getType(x,y);
		}
		return null;
	}


	public double scaleAccurateValues(double position, double scale) {
		return (position / scale);
	}
}
