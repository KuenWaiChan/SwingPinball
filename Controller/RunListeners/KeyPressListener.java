package Controller.RunListeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyPressListener implements KeyListener {

	private AutoTickToggleListener tucker;

	public KeyPressListener(AutoTickToggleListener tucker) {
		this.tucker = tucker;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		tucker.keyDownUpdate(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		tucker.keyUpUpdate(e);
	}
}

