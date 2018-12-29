package Controller.RunListeners;

import View.GUIPanel.iRunGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefreshBoardListener implements ActionListener {
	private iRunGUI runGui;

	public RefreshBoardListener(iRunGUI runGui) {
		this.runGui = runGui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		runGui.resetBordState();
		runGui.toggleRunState(false);
	}
}
