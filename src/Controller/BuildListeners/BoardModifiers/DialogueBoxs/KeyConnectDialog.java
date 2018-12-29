package Controller.BuildListeners.BoardModifiers.DialogueBoxs;

import Controller.BuildListeners.BoardModifiers.IKeyConnectListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyConnectDialog extends JFrame {
	JLabel key;
	private Integer keyStroke;
	ButtonGroup group;
	private IKeyConnectListener master;

	public KeyConnectDialog(IKeyConnectListener masterListener) {
		master = masterListener;

		keyStroke = null;
		this.setType(Type.UTILITY);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));

		JPanel panelMinor = new JPanel();
		panelMinor.setLayout(new GridLayout(0, 2));
		panelMinor.add(new JLabel("Key:"));
		key = new JLabel();
		panelMinor.add(key);
		panel.add(panelMinor);

		JRadioButton up = new JRadioButton("Key up");
		up.setActionCommand("up");
		JRadioButton down = new JRadioButton("Key down");
		down.setActionCommand("down");
		up.setFocusable(false);
		down.setFocusable(false);
		group = new ButtonGroup();
		group.add(up);
		group.add(down);
		panel.add(up);
		panel.add(down);
		JButton hellp = new JButton("Submit");
		hellp.addActionListener(e -> submit());
		panel.add(hellp);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> master.cancel());
		panel.add(cancel);
		hellp.setFocusable(false);
		cancel.setFocusable(false);
		this.requestFocus();

		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				keyUpdate(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
		this.getContentPane().add(panel);
		this.setSize(new Dimension(150, 300));
		this.setVisible(true);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(d.width / 2 - this.getWidth() / 2, d.height / 2 - this.getHeight() / 2);
	}

	private void keyUpdate(KeyEvent e) {
		keyStroke = e.getKeyCode();
		key.setText("" + keyStroke);
	}


	private void submit() {
		if (keyStroke != null) {
			ButtonModel selected = group.getSelection();
			if (selected != null)
				master.submitData(keyStroke, selected.getActionCommand());
		}
	}

}