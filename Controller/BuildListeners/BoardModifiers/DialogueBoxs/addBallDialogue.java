package Controller.BuildListeners.BoardModifiers.DialogueBoxs;

import Model.IGizmoModel;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import View.Styliser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class addBallDialogue {
	private IGizmoModel model;
	private double rcx;
	private double rcy;
	private String name;
	private JDialog display;
	private JComboBox<String> target;

	public addBallDialogue(IGizmoModel model, double rcx, double rcy) {
		this.model = model;
		this.rcx = rcx;
		this.rcy = rcy;
		if (model.getType(rcx, rcy) == GizmoType.Absorber)
			initialise();
	}

	private void initialise() {
		display = new JDialog();
		display.setResizable(false);
		display.setTitle("Add Ball");


		Container pain = display.getContentPane();
		pain.setLayout(new GridLayout(0, 1));
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));

		generateSet();
		name = model.getName(rcx,rcy);
		p.add(new JLabel(name));
		p.add(target);

		pain.add(p);

		JButton b = new JButton("Submit");
		b.addActionListener(e -> submit());
		pain.add(b);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		display.setLocation(d.width / 2 - display.getWidth() / 2, d.height / 2 - display.getHeight() / 2);
		display.setSize(250, 120);
		display.setVisible(true);
	}

	private void submit(){
		model.executeInstruction(new DefaultInstructions.addBallToAbsorber(name, (String) target.getSelectedItem()));
		display.dispose();
	}

	private void generateSet()
	{
		target = new JComboBox<>();
		for (String s : model.getFreeBallNames())
			target.addItem(s);
		Styliser.componentStyler(target);
	}
}
