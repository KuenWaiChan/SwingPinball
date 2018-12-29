package Controller.RunListeners;

import Model.IGizmoModel;
import Model.util.DefaultInstructions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ForceAdditionMouseListener implements MouseListener {
	private IGizmoModel model;
	private double x1, y1, x2, y2;

	public ForceAdditionMouseListener(IGizmoModel model) {
		this.model = model;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();

		double
				dX = (x2 - x1) / 10,
				dY = (y2 - y1) / 10;
		if (model != null)
			model.executeInstruction(new DefaultInstructions.GeneralVelocity(dX, dY));
	 
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public void setModel(IGizmoModel model) {
		this.model = model;
	}
}
