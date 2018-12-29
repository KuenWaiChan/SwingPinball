package Controller.BuildListeners.BoardModifiers;

import Controller.BuildListeners.BoardModifiers.DialogueBoxs.KeyConnectDialog;
import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import View.GUIPanel.BuildGui;
import View.GUIPanel.iBuildGUI;

public class KeyConnectListener extends ABoardModifier implements IKeyConnectListener {
	private Integer keyStroke;
	private String direction;
	private boolean submitted, approved;

	public KeyConnectListener(iBuildGUI buildGui, IGizmoModel model) {
		super(buildGui, model);
	}

	private KeyConnectDialog d;

	@Override
	Instruction getInstruction(double x, double y) {
		if (model.getType(x, y) == null)
			return null;
		submitted = false;
		if (openDialogue()) {
			return new DefaultInstructions.KeyConnect(keyStroke, direction, model.getName(x, y));
		} else {
			return null;
		}

	}

	@Override
	public void cancel() {
		submitted = true;
		approved = false;
	}

	private boolean openDialogue() {
		d = new KeyConnectDialog(this);
		while (!submitted) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ignored) {
			}
		}
		d.dispose();
		d = null;
		return approved;
	}


	public void submitData(Integer keyStroke, String actionCommand) {
		submitted = true;
		approved = true;
		this.keyStroke = keyStroke;
		this.direction = actionCommand;
	}

}
