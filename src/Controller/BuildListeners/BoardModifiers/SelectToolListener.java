package Controller.BuildListeners.BoardModifiers;

import Controller.BuildListeners.BoardModifiers.DialogueBoxs.connectionDialogue;
import Model.IGizmoModel;
import Model.util.GizmoType;
import View.GUIPanel.iBuildGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class SelectToolListener implements ActionListener {
	private LinkedList<String> tools;
	private IGizmoModel model;
	private JComboBox<String> toolBox;
	private iBuildGUI master;

	public SelectToolListener(iBuildGUI master, IGizmoModel model) {
		this.master = master;
		tools = new LinkedList<>();

		tools.add("Rotate");
		tools.add("Shift");
		tools.add("Connect Tool");
		tools.add("Disconnect Tool");
		tools.add("Key Connect Tool");
		tools.add("Delete");
		
		this.model = model;
		this.toolBox = new JComboBox<>();
		for (String s : tools)
			toolBox.addItem(s);
		
		
		toolBox.addActionListener(this);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		////("SelectGizmoListener: What is that building????!!!???");
		String s = (String) toolBox.getSelectedItem();
		if (s == null) {
		
		}else if (s.equals("Rotate")) {
			master.setMouseClick(new RotateGizmoListener(master, model));
		} else if (s.equals("Shift")) {
			master.setMouseClick(new MoveGizmoListener(master, model));
		}else if (s.equals("Connect Tool")) {
			new connectionDialogue(model, true);
		}else if (s.equals("Disconnect Tool")) {
			new connectionDialogue(model, false);
		}else if (s.equals("Key Connect Tool")) {
			master.setMouseClick(new KeyConnectListener(master, model));
		}else if (s.equals("Delete")) {
			master.setMouseClick(new DeleteGizmoListener(master, model));
		}
		master.refresh();
	}
	public JComboBox<String> getTools() {
		return toolBox;
	}
}
