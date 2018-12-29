package Controller.BuildListeners.BoardModifiers.DialogueBoxs;

import Controller.BuildListeners.BoardModifiers.DialogueBoxs.DialogueComplexListeners.ConnectionHiglighter;
import Controller.BuildListeners.BoardModifiers.instructionListenerFactoria;
import Model.IGizmoModel;
import View.Styliser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class connectionDialogue
{
	JDialog display;
	private IGizmoModel model;
	private JComboBox<String> origin;
	private JComboBox<String> target;
	ConnectionHiglighter originHighlight;
	ConnectionHiglighter targetHighlight;
private boolean addConnection;

	public connectionDialogue(IGizmoModel model, Boolean addConnection)
	{
		this.model = model;
		this.addConnection = addConnection;
		initialise();

	}

	public connectionDialogue(IGizmoModel model, String origin,Boolean addConnection)
	{
		this.model = model;
		this.addConnection = addConnection;
		initialise();
		this.origin.setSelectedItem(origin);
	}

	private void initialise()
	{
		display = new JDialog();
		display.setResizable(false);
		display.setTitle("Connect");

		Container pain = display.getContentPane();
		pain.setLayout(new GridLayout(0, 1));
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		generateSets();
		originHighlight = new ConnectionHiglighter(origin, model);
		targetHighlight = new ConnectionHiglighter(target, model);
		origin.addActionListener(originHighlight);
		target.addActionListener(targetHighlight);
		p.add(origin);
		p.add(target);

		JPanel q = new JPanel();
		q.setLayout(new GridLayout(1, 2));
		JButton submit = new JButton("Submit");
		JButton cancel = new JButton("Close");
		submit.addActionListener(e -> submit());
		cancel.addActionListener(e -> cancel());
		Styliser.componentStyler(submit);
		Styliser.componentStyler(cancel);

		q.add(submit);
		q.add(cancel);

		pain.add(p);
		pain.add(q);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		display.setLocation(d.width / 2 - display.getWidth() / 2, d.height / 2 - display.getHeight() / 2);
		display.setSize(250, 120);
		display.setVisible(true);
	}

	void submit()
	{
		instructionListenerFactoria.setModel(model);

		if (addConnection) {
			instructionListenerFactoria.connectGizmos((String) origin.getSelectedItem(), (String) target.getSelectedItem());
		} else {
			instructionListenerFactoria.disconnectGizmos((String) origin.getSelectedItem(), (String) target.getSelectedItem());
		}

	}

	void cancel()
	{
		originHighlight.reset();
		targetHighlight.reset();
		display.dispose();
	}

	private void generateSets()
	{
		origin = new JComboBox<>();
		target = new JComboBox<>();
		List<String> ls = model.getNames();
		for (String s : ls) {
			origin.addItem(s);
			target.addItem(s);
		}
		Styliser.componentStyler(origin);
		Styliser.componentStyler(target);
	}

}
