package Controller.FrameListeners;

import View.MainView.iGBallMainView;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToggleModeListener implements ActionListener {

private iGBallMainView mainView;

	public ToggleModeListener(iGBallMainView mainView){
		this.mainView = mainView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainView.toggleMode();
		mainView.display(mainView.isBuildMode() ? "Build mode" : "Run mode");
	}


}
