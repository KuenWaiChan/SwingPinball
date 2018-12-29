package Controller.FrameListeners;

import Model.IGizmoModel;
import View.MainView.iGBallMainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveBoardListener implements ActionListener {
	private iGBallMainView master;

	public SaveBoardListener(iGBallMainView master) {
		this.master = master;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
			try {
				master.save(jfc.getSelectedFile().getAbsolutePath());
				master.display("Saved " + jfc.getSelectedFile().getAbsolutePath());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
