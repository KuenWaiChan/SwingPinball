package View.GUIPanel;

import Controller.BuildListeners.GlobalModifiers.*;
import Controller.RunListeners.*;
import Model.IGizmoModel;
import View.Board.RunBoard;
import View.MainView.iGBallMainView;
import View.Styliser;

import javax.swing.*;
import java.awt.*;

public class RunGui extends GBallGui implements iRunGUI {
	private JButton play;
	private IGizmoModel model;
	private iGBallMainView master;
	private RunBoard board;
	private AutoTickToggleListener ticka;

	public RunGui(IGizmoModel model, iGBallMainView master) {
		super();
		this.model = model;
		this.master = master;
		sideMenuArea.add(sideMenuAreaSetup());
		sideFrame.hide();
	}

	public void setBoard(RunBoard board) {
		this.board = board;
	}

	@Override
	JPanel sideMenuAreaSetup() {
		JPanel returnPanel = new JPanel(new BorderLayout(6, 6));

		JPanel menuPane = new JPanel();
		BoxLayout boxLayout = new BoxLayout(menuPane, BoxLayout.Y_AXIS);
		menuPane.setLayout(boxLayout);

		JPanel optionPanel = new JPanel();
		JPanel returnToBuildPanel = new JPanel();

		optionPanel.setBorder(buttonBorderGenerator("Options"));

		JPanel tempFlowPanel = new JPanel(new GridLayout(0, 1, 6, 6));

		play = new JButton("Play");
		ticka = new AutoTickToggleListener(model, this);
		play.addActionListener(ticka);


		KeyPressListener kpl = new KeyPressListener(ticka);
		this.addKeyListener(kpl);
		master.setKeyListener(kpl);
		tempFlowPanel.add(play);
		Styliser.componentStyler(play);

		JButton tick = new JButton("Step");
		tick.addActionListener(new SingleTickListener(model));
		tempFlowPanel.add(tick);
		Styliser.componentStyler(tick);

		JButton reload = new JButton("Reload Board");
		reload.addActionListener(new RefreshBoardListener(this));
		tempFlowPanel.add(reload);
		Styliser.componentStyler(reload);

		optionPanel.add(tempFlowPanel);

		menuPane.add(optionPanel);
		menuPane.add(returnToBuildPanel);


		JPanel environmentPanel = new JPanel();
		environmentPanel.setLayout(new GridLayout(0,1));
		environmentPanel.setBorder(buttonBorderGenerator("Environment"));

		JPanel gravityPanel = new JPanel();
		gravityPanel.setBorder(buttonBorderGenerator("Gravity"));
		GravityListener g = new GravityListener(model);
		gravityPanel.add(g.createSlider());

		JPanel timePanel = new JPanel();
		timePanel.setBorder(buttonBorderGenerator("Time"));
		SetTimeListener time = new SetTimeListener(model);
		timePanel.add(time.createSlider());

		JPanel windPanel = new JPanel();
		windPanel.setBorder(buttonBorderGenerator("Wind"));
		WindListener w=new WindListener(model);
		windPanel.add(w.createSlider());

		JPanel absorptionPanel = new JPanel();
		absorptionPanel.setBorder(buttonBorderGenerator("Absorption"));
		AbsorptionListener l = new AbsorptionListener(model);
		absorptionPanel.add(l.createSlider());

		JPanel frictionPanel = new JPanel();
		frictionPanel.setLayout(new GridLayout(0,1));
		frictionPanel.setBorder(buttonBorderGenerator("Friction"));

		FrictionListener f = new FrictionListener(model);

		JPanel mu1Panel = new JPanel();
		mu1Panel.setBorder(buttonBorderGenerator("Friction MU1"));
		JPanel mu2Panel = new JPanel();
		mu2Panel.setBorder(buttonBorderGenerator("Friction MU2"));

		JSlider mu1 = f.createYSlider();
		JSlider mu2 = f.createSlider();

		mu1.setToolTipText("Set mu1 value");
		mu2.setToolTipText("Set mu2 value");

		mu1Panel.add(mu1);
		mu2Panel.add(mu2);

		environmentPanel.add(gravityPanel);
		environmentPanel.add(windPanel);
		environmentPanel.add(timePanel);
		environmentPanel.add(absorptionPanel);
		environmentPanel.add(mu1Panel);
		environmentPanel.add(mu2Panel);
		JButton grid = new JButton("Toggle Grid");
		grid.addActionListener(new GridToggleListener(this));
		Styliser.componentStyler(grid);
		environmentPanel.add(grid);

		menuPane.add(environmentPanel);
		returnPanel.add(menuPane);

		return returnPanel;
	}

	/**
	 * Set whether to draw the lines or not!
	 * @param isDrawing
	 */
	@Override
	public void setDrawLines(boolean isDrawing) {
		if (board != null)
			board.setDrawLines(isDrawing);
	}


	/**
	 * Sets the model and resets the side pannel
	 * @param interfaceCopy
	 */
	public void setModel(IGizmoModel interfaceCopy) {
		this.model = interfaceCopy;
		resetSidePanel();
	}

	/**
	 * Clears the side panel and then recreates it
	 */
	private void resetSidePanel() {
		sideMenuArea.removeAll();
		sideMenuArea.add(sideMenuAreaSetup());
		sideMenuArea.validate();
	}

	@Override
	public void toggleRunState(Boolean isRunning) {
		if (isRunning) {
			play.setText("Pause");
		} else {
			play.setText("Play");
		}
	}


	/**
	 * Stop the model if its currently running and reset it.
	 */
	@Override
	public void resetBordState() {
		stop();
		master.reSetRun();
	}

	/**
	 * Stops the timer if it is running
	 */
	public void stop() {
		ticka.stop();
	}
}