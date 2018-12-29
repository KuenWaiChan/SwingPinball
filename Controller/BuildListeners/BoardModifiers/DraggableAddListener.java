package Controller.BuildListeners.BoardModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import Model.util.GizmoType;
import View.GUIPanel.iBuildGUI;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class DraggableAddListener extends AddGizmoListener {
	private double x, y;

	public DraggableAddListener(iBuildGUI buildGui, IGizmoModel model, GizmoType type) {
		super(buildGui, model, type);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		x = scaleValues(e.getX(), buildGui.getScaleX());
		y = scaleValues(e.getY(), buildGui.getScaleY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		double x2 = scaleValues(e.getX(), buildGui.getScaleX());
		double y2 = scaleValues(e.getY(), buildGui.getScaleY());
		double top, left, height, length;
		if (SwingUtilities.isRightMouseButton(e)) {
			callPopup(e, x2, y2);
		} else {
			if (x < x2) {
				top = x;
				length = x2 - x;
			} else {
				top = x2;
				length = x - x2;
			}
			if (y < y2) {
				left = y;
				height = y2 - y;
			} else {
				left = y2;
				height = y - y2;
			}

			if (top < 0 || left < 0 || left + height > 20 || top + length > 20)
				return;
			Instruction i = getInstruction(top, left, length + 1, height + 1);
			if (i != null) {
				model.executeInstruction(i);
			}
		}
	}


}
