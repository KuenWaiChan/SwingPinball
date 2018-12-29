package Controller.FrameListeners;

import View.MainView.iGBallMainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewBoardListener implements ActionListener {
	private iGBallMainView master;

	public NewBoardListener(iGBallMainView master) {
		this.master = master;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(	JOptionPane.showConfirmDialog (null, "Create new board?","New File", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			master.clear();
			master.display("Cleared");
		}
	}
}
