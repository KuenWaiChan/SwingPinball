package View.Board;

import Controller.RunListeners.ForceAdditionMouseListener;
import Model.IGizmoModel;

import javax.swing.*;
import java.awt.*;

public class RunBoard extends Board {
	private boolean drawLines;
		ForceAdditionMouseListener faml;
	public RunBoard(JPanel parent, IGizmoModel model) {
		super(parent, model);
		faml = new ForceAdditionMouseListener(model);
		this.addMouseListener(faml);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (drawLines) // If we want to draw the grid
			getDrawer().drawGrid(g, scaleX, scaleY, this.getHeight(), this.getWidth());
	}

	public void setDrawLines(boolean haveLines) {
		this.drawLines = haveLines;
	}

	/**
	 * Same as super just will add it to any listeners that need it
	 * @param model != null
	 */
	@Override
	public void setModel(IGizmoModel model) {
		super.setModel(model);
		if (model != null)
			faml.setModel(model); //needs model to run instructions
	}
}
