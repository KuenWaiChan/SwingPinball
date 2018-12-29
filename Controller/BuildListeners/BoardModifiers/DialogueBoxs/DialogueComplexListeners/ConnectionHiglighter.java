package Controller.BuildListeners.BoardModifiers.DialogueBoxs.DialogueComplexListeners;

import Model.Board.ConcreteGizmoModel;
import Model.IGizmoModel;
import Model.Instruction;
import Model.ProtoGizmo;
import Model.util.DefaultInstructions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionHiglighter implements ActionListener {
	private JComboBox<String> origin;
	private IGizmoModel model;
	private Instruction i;
	public ConnectionHiglighter(JComboBox<String> origin, IGizmoModel model) {
		this.origin = origin;
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model.executeInstruction(getInstruction((String) origin.getSelectedItem()));
	}

	Instruction getInstruction(String s) {
		armGizmo(s,Color.CYAN);
		return concreteModel -> {
			if (oldGizmo != null)
				concreteModel.getGizmo(oldGizmo).setColor(oldColor);
			if (newGizmo != null) {
				oldGizmo = newGizmo;
				oldColor = concreteModel.getGizmo(oldGizmo).getColor();
				concreteModel.getGizmo(oldGizmo).setColor(newColor);
			}};
	}

		private Color oldColor;
		private String oldGizmo;
		private Color newColor;
		private String newGizmo;

		public void armGizmo(String name, Color c) {
			if (name == null || c == null) {
				newColor = oldColor;
				newGizmo = oldGizmo;
			} else {
				newColor = c;
				newGizmo = name;
			}

		}

		public void reset(){
			model.executeInstruction(getInstruction(null));
		}

}
