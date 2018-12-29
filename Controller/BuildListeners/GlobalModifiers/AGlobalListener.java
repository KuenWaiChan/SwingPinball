package Controller.BuildListeners.GlobalModifiers;

import Model.IGizmoModel;
import Model.Instruction;
import View.Styliser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

public abstract class AGlobalListener implements ChangeListener {
	private JSlider val;
	private IGizmoModel model;
	protected int MAX, MIN, START, SCALE;
	public AGlobalListener (IGizmoModel model, int max, int min, int start, int scale){
		this.model = model;
		MAX = max;
		MIN = min;
		START = start;
		SCALE = scale;
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		//("Global Listener : Aut viam inveniam aut faciam");
		model.executeInstruction(getInstruction((double) val.getValue()/SCALE));
	}

	public JSlider createSlider(){
		this.val = genSlider();
		return this.val;
	}

	protected JSlider genSlider(){
		JSlider gravity = new JSlider(JSlider.HORIZONTAL, MIN, MAX, START);
		gravity.addChangeListener(this);
		gravity.setMajorTickSpacing(SCALE);
		gravity.setMinorTickSpacing(1);
		Hashtable<Integer, JLabel> table = new Hashtable<>();
		for(int i = MIN; i <= MAX; i += SCALE){
			table.put(i, new JLabel("" + i/SCALE));
		}
		gravity.setLabelTable(table);
		gravity.setPaintLabels(true);
		gravity.setPaintTicks(true);
		Styliser.componentStyler(gravity);
		return gravity;
	}

	abstract Instruction getInstruction(double value);
}
