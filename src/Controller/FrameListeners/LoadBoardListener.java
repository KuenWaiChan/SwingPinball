package Controller.FrameListeners;

import Model.IGizmoModel;
import View.MainView.iGBallMainView;
import java.io.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadBoardListener implements ActionListener {
	private iGBallMainView gBallView;

	public LoadBoardListener(iGBallMainView gBallView) {
		this.gBallView = gBallView;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			try {
				gBallView.clear();
				gBallView.loadFile(jfc.getSelectedFile().getAbsolutePath());
			} catch (FileNotFoundException ex) {
				// silently fail, since it is impossible to get wrong path from FileChooser
				ex.printStackTrace();
			}

			gBallView.update();
			gBallView.display("Loaded : " + jfc.getSelectedFile().getAbsolutePath());
		}
	}
}
