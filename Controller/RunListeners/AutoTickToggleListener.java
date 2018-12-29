package Controller.RunListeners;

import Model.IGizmoModel;
import View.GUIPanel.iRunGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;

public class AutoTickToggleListener implements ActionListener {

	private IGizmoModel model;
	private iRunGUI runGui;
	private Timer timer;
	private Set<Integer> keyUpPresses;
	private Set<Integer> keyDownPresses;
	public AutoTickToggleListener(IGizmoModel model, iRunGUI runGui) {
		this.runGui = runGui;
		this.model = model;
		keyUpPresses = new HashSet<>();
		keyDownPresses = new HashSet<>();
		timer = new Timer(25, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == timer && model.isRunning()) {
			model.tick(keyUpPresses, keyDownPresses);
			keyUpPresses.clear();
			keyDownPresses.clear();
			return;
		}

		if (model.isRunning()) {
			timer.stop();
			model.stop();
		} else {
			timer.start();
			model.run();
		}
		runGui.toggleRunState(model.isRunning());
	}


	public void stop(){
		timer.stop();
		model.stop();
	}

	public void keyUpUpdate(KeyEvent e) {
		keyUpPresses.add(e.getKeyCode());
	}

	public void keyDownUpdate(KeyEvent e) {
		keyDownPresses.add(e.getKeyCode());
	}
}
