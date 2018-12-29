package View.GUIPanel;


import Controller.BuildListeners.BoardModifiers.*;
import Controller.BuildListeners.BoardModifiers.ContextualListeneers.*;
import Controller.BuildListeners.GlobalModifiers.*;
import Model.IGizmoModel;
import Model.util.GizmoType;
import View.Board.Board;
import View.Styliser;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BuildGui extends GBallGui implements iBuildGUI {

    private IGizmoModel model;
    private Board playArea;
    private JPopupMenu contextMenu;
    private Pair<Double, Double> contextMenuCoord;
    private MouseListener old;
    private MouseListener newListener;
    private double rightClickX, rightClickY;
@Override
    public void refresh() {
        repaint();
    }

    public BuildGui(IGizmoModel model, Board playArea) {
        super();
        this.model = model;
        this.playArea = playArea;
        sideMenuArea.add(sideMenuAreaSetup());
    }

    /**
     * Creates the side menu area for the build gui
     * Adds gizmo selecter from the SelectGizmoListener
     * Adds tool selecter from the SelectToolListener
     * Each of the sliders is created in their listener
     * @return JPanel created after setup
     */
    JPanel sideMenuAreaSetup() {
        JPanel returnPanel = new JPanel(new BorderLayout(6, 6));

        JPanel menuPane = new JPanel();
        BoxLayout boxLayout = new BoxLayout(menuPane, BoxLayout.Y_AXIS);
        menuPane.setLayout(boxLayout);
        Styliser.panelStyler(menuPane);
        JPanel gizmoPanel = new JPanel();
        JPanel ballPanel = new JPanel();
        JPanel toolPanel = new JPanel();
        JPanel environmentPanel = new JPanel();
        JPanel executePanel = new JPanel();

        gizmoPanel.setBorder(buttonBorderGenerator("Gizmos"));
        ballPanel.setBorder(buttonBorderGenerator("Ball"));
        toolPanel.setBorder(buttonBorderGenerator("Tools"));
//        environmentPanel.setBorder(buttonBorderGenerator("Environment"));

        JPanel tempFlowPanel = new JPanel(new GridLayout(0, 1, 6, 6));
        SelectGizmoListener sgl = new SelectGizmoListener(this, model);
        JComboBox<String> Gizmos = sgl.getGizmos();
        ////////System.out.println.out.println("BuildGui : " + Gizmos.getItemCount());
        tempFlowPanel.add(Gizmos);
        Styliser.componentStyler(Gizmos);
        gizmoPanel.add(tempFlowPanel);


        tempFlowPanel = new JPanel(new GridLayout(0, 1, 6, 6));
        SelectToolListener stl = new SelectToolListener(this, model);
        JComboBox<String> Tools = stl.getTools();
        tempFlowPanel.add(Tools);
        Styliser.componentStyler(Tools);
        toolPanel.add(tempFlowPanel);
        tempFlowPanel = new JPanel(new GridLayout(0, 1, 6, 6));

        JButton placeBall = new JButton("Place Ball");
        Styliser.componentStyler(placeBall);
        AddBallListener ballZ = new AddBallListener(this, model, GizmoType.Ball);
        placeBall.addActionListener(ballZ);
        tempFlowPanel.add(placeBall);

        JPanel ballXPanel = new JPanel();
        ballXPanel.setBorder(buttonBorderGenerator("Ball VX"));
        ballXPanel.add(ballZ.getSlideX());

        JPanel ballYPanel = new JPanel();
        ballYPanel.setBorder(buttonBorderGenerator("Ball VY"));
        ballYPanel.add(ballZ.getSlideY());

        tempFlowPanel.add(ballYPanel);
        tempFlowPanel.add(ballXPanel);
        ballPanel.add(tempFlowPanel);
        tempFlowPanel = null; //Needed otherwise the ballX does not show

        environmentPanel.setLayout(new GridLayout(0, 1));
        JPanel gravityPanel = new JPanel();
        gravityPanel.setBorder(buttonBorderGenerator("Gravity"));
        GravityListener g = new GravityListener(model);
        gravityPanel.add(g.createSlider());



        JPanel windPanel = new JPanel();
        windPanel.setBorder(buttonBorderGenerator("Wind"));
        WindListener w = new WindListener(model);
        windPanel.add(w.createSlider());

        JPanel timePanel = new JPanel();
        timePanel.setBorder(buttonBorderGenerator("Time"));
        SetTimeListener time = new SetTimeListener(model);
        timePanel.add(time.createSlider());

        JPanel absorptionPanel = new JPanel();
        absorptionPanel.setBorder(buttonBorderGenerator("Absorption"));

        AbsorptionListener l = new AbsorptionListener(model);
        absorptionPanel.add(l.createSlider());

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

        environmentPanel.add(mu1Panel);
        environmentPanel.add(mu2Panel);
        environmentPanel.add(gravityPanel);
        environmentPanel.add(timePanel);
        environmentPanel.add(windPanel);
        environmentPanel.add(absorptionPanel);


        menuPane.add(gizmoPanel);
        menuPane.add(toolPanel);
        menuPane.add(ballPanel);
        menuPane.add(environmentPanel);
        menuPane.add(executePanel);

        returnPanel.add(menuPane);
        return returnPanel;
    }

    /**
     * Sets model and resets the side pannel
     *
     * @param interfaceCopy
     */
    @Override
    public void setModel(IGizmoModel interfaceCopy) {
        this.model = interfaceCopy;
        sideMenuArea.removeAll();
        sideMenuArea.add(sideMenuAreaSetup());
    }

    /**
     * removes old mouse click and replaces it with the new mouse listener parameter
     * @param listener != null
     */
    @Override
    public void setMouseClick(MouseListener listener) {
        playArea.removeMouseListener(old);
        old = listener;
        newListener = listener;
        playArea.addMouseListener(listener);
    }


    /**
     * Gets the scale of the current board and then returns it
     * @return double, the scale of the X axis of the board
     */
    @Override
    public double getScaleX() {
        return playArea.getScaleX();
    }

    /**
     * Gets the scale of the current board and then returns it
     * @return double, the scale of the Y axis of the board
     */
    @Override
    public double getScaleY() {
        return playArea.getScaleY();
    }

    /**
     * The context menu of the GUI is called from the mouse listeners
     * Displays a menu depending on what type of gizmo is inputted
     * The menu is spawned at (XGridCoord,YGridCoord)
     * The menu displays the name of the gizmo at the top
     * @param type GizmoType
     * @param e The event that called this function
     * @param XGridCoord the x position to display the menu
     * @param YGridCoord the y position to display the menu
     * @param name the name of the gizmo this menu is acting on
     */
    public void popUpMenu(GizmoType type, MouseEvent e, double XGridCoord, double YGridCoord, String name) {
        contextMenuCoord = new Pair<Double, Double>(XGridCoord, YGridCoord);
        ////System.out.println.out.println("X:" + XGridCoord + ", Y:" + YGridCoord);
        rightClickX = XGridCoord;
        rightClickY = YGridCoord;
        contextMenu = new JPopupMenu();
        contextMenu.add(new JLabel(name));
        if (!(type == GizmoType.Ball)) {
            JMenuItem temp = new JMenuItem("Delete");
            contextMenu.add(temp);
            temp.addActionListener(new ContextualDeleteListener(this, model));

            if(!(type == GizmoType.Absorber)) {
                temp = new JMenuItem("Rotate");
                contextMenu.add(temp);
                temp.addActionListener(new ContextualRotateListener(this, model));

                temp = new JMenuItem("Move");
                contextMenu.add(temp);
                temp.addActionListener(new ContextualMoveListener(this, model));
            } else {
                temp = new JMenuItem("Set Ball");
                contextMenu.add(temp);
                temp.addActionListener(new ContextualAddBallAbsorberListener(this, model));
            }
            temp = new JMenuItem("Connection");
            contextMenu.add(temp);
            temp.addActionListener(new ContextualConnectionListener(this, model));

            temp = new JMenuItem("Disconnect");
            contextMenu.add(temp);
            temp.addActionListener(new ContextualDisconnectionListener(this, model));

            temp = new JMenuItem("KeyConnect");
            contextMenu.add(temp);
            temp.addActionListener(new ContextualKeyConnectListener(this, model));
        } else {
            JMenuItem temp = new JMenuItem("Ball Speed");
            contextMenu.add(temp);
            temp.addActionListener(new ContextualBallSpeedListener(name, model));
        }

        contextMenu.show(e.getComponent(), e.getX(), e.getY());

    }


    /**
     * Gets the current position of the current context menu
     * @return Pair<Double,Double> the coordinates of the context menu
     */
    public Pair<Double, Double> getContextCoord() {
        return new Pair<>(contextMenuCoord.getKey(), contextMenuCoord.getValue());
    }

    public ABoardModifier getListener() {
        return (ABoardModifier) newListener;
    }

    public double getRightClickX() {
        return rightClickX;
    }

    public double getRightClickY() {
        return rightClickY;
    }
}
