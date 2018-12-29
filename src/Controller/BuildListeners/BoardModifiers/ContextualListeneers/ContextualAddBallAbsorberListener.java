package Controller.BuildListeners.BoardModifiers.ContextualListeneers;

import Controller.BuildListeners.BoardModifiers.DialogueBoxs.addBallDialogue;
import Model.IGizmoModel;
import View.GUIPanel.BuildGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContextualAddBallAbsorberListener implements ActionListener {
	private BuildGui buildGui;
	private IGizmoModel model;

	public ContextualAddBallAbsorberListener(BuildGui buildGui, IGizmoModel model) {
		this.buildGui = buildGui;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		double rcx = buildGui.getRightClickX();
		double rcy = buildGui.getRightClickY();
		new addBallDialogue(model, rcx, rcy);
	}
}
