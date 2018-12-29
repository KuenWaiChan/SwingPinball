package Controller.RunListeners;

import Model.IGizmoModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SingleTickListener implements ActionListener {
	private IGizmoModel model;

	public SingleTickListener(IGizmoModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!model.isRunning()) model.tick(null, null);
	}
}
