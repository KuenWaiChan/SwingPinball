package Controller.RunListeners;

import View.GUIPanel.RunGui;
import View.GUIPanel.iRunGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridToggleListener implements ActionListener {
	private iRunGUI runGui;
	private boolean isGridded;
	public GridToggleListener(iRunGUI runGui) {
		this.runGui = runGui;
		isGridded = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		runGui.setDrawLines((isGridded=!isGridded));
	}


}
