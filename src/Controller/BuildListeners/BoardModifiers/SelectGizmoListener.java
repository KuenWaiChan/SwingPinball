package Controller.BuildListeners.BoardModifiers;

import Model.IGizmoModel;
import Model.util.GizmoType;
import View.GUIPanel.iBuildGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class SelectGizmoListener implements ActionListener {
	private LinkedList<String> nonDragable;
	private LinkedList<String> dragable;
	private IGizmoModel model;
	private JComboBox<String> gizmoBox;
	private iBuildGUI master;

	public SelectGizmoListener(iBuildGUI master, IGizmoModel model) {
		this.master = master;
		nonDragable = new LinkedList<>();
		dragable = new LinkedList<>();
		nonDragable.add("Square");
		nonDragable.add("Circle");
		nonDragable.add("Triangle");
		nonDragable.add("LeftFlipper");
		nonDragable.add("RightFlipper");
		dragable.add("Absorber");
		this.model = model;
		this.gizmoBox = new JComboBox<>();
		for (String s : model.getNonDraggableGizmoTypes())
			gizmoBox.addItem(s);
		for (String s : model.getDraggableGizmoTypes())
			if (!nonDragable.contains(s))
				gizmoBox.addItem(s);
		gizmoBox.addActionListener(this);
		setMasterMouseClick(dragable.get(0));
	}


	public JComboBox<String> getGizmos() {
		return gizmoBox;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		////("SelectGizmoListener: What is that building????!!!???");
		String s = (String) gizmoBox.getSelectedItem();
		if (s != null)
			setMasterMouseClick(s);
	}

	private void setMasterMouseClick(String s) {
		if (nonDragable.contains(s))
			master.setMouseClick(new AddGizmoListener(master, model, GizmoType.valueOf(s)));
		if (dragable.contains(s))
			master.setMouseClick(new DraggableAddListener(master, model, GizmoType.valueOf(s)));

	}
}
