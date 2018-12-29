package View;

import javax.swing.*;

public class MessageDisplay {
	
	private JTextArea scroller;

	public MessageDisplay(String s){
		intialise();
		scroller.setText(s);
	}

	private void intialise() {
		JFrame frame = new JFrame("Build Mode");
		frame.setSize(300,600);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		scroller = new JTextArea();
		scroller.setEditable(false);
	}
}
