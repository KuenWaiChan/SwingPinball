package Controller.BuildListeners.BoardModifiers.ContextualListeneers;

import Controller.BuildListeners.BoardModifiers.DialogueBoxs.ballSpeedModifierDialogue;
import Model.IGizmoModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContextualBallSpeedListener implements ActionListener {
	private String name;
	private IGizmoModel model;

	public ContextualBallSpeedListener(String name, IGizmoModel model) {
		this.name = name;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new ballSpeedModifierDialogue(model,name);
	}
}
