package Controller.BuildListeners.BoardModifiers.DialogueBoxs;

import Model.IGizmoModel;
import Model.util.DefaultInstructions;
import View.Styliser;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ballSpeedModifierDialogue {
	JDialog display;
	private int MIN = -50,MAX = 50,START = 0,SCALE = 10;
	private IGizmoModel model;
	private String name;

	JSlider sliderX;
	JSlider sliderY;

	public ballSpeedModifierDialogue(IGizmoModel model, String name) {
		this.model = model;
		this.name = name;
		initialise();
	}

	private void submit () {
		model.executeInstruction( new DefaultInstructions.ChangeGizmoVelocity(name, sliderX.getValue(), sliderY.getValue()));
	}

	private void initialise() {
		display = new JDialog();
		display.setTitle("Ball");
		display.setResizable(false);
		display.setLayout(new GridLayout(0,1));

		createSliders();

		JButton submit = new JButton("Submit");
		submit.addActionListener(e -> submit());
		Styliser.componentStyler(submit);

		display.add(new JLabel("X:"));
		display.add(sliderX);
		display.add(new JLabel("Y:"));
		display.add(sliderY);
		display.add(submit);



		display.setVisible(true);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		display.setLocation(d.width / 2 - display.getWidth() / 2, d.height / 2 - display.getHeight() / 2);
		display.setSize(250, 240);
	}

	private void createSliders(){
		sliderX = new JSlider(JSlider.HORIZONTAL, MIN, MAX, START);
		sliderY = new JSlider(JSlider.HORIZONTAL, MIN, MAX, START);
		sliderX.setMajorTickSpacing(SCALE);
		sliderX.setMinorTickSpacing(1);
		sliderY.setMajorTickSpacing(SCALE);
		sliderY.setMinorTickSpacing(1);
		Hashtable<Integer, JLabel> table = new Hashtable<>();
		for(int i = MIN; i <= MAX; i += SCALE){
			table.put(i, new JLabel("" + i/SCALE));
		}

		sliderX.setLabelTable(table);
		sliderX.setPaintLabels(true);
		sliderX.setPaintTicks(true);

		sliderY.setLabelTable(table);
		sliderY.setPaintLabels(true);
		sliderY.setPaintTicks(true);

		Styliser.componentStyler(sliderX);
		Styliser.componentStyler(sliderY);
	}
}
