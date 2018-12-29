package Controller.BuildListeners.BoardModifiers;

import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.Instruction;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import View.GUIPanel.iBuildGUI;
import View.Styliser;
import physics.Vect;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

public class AddBallListener extends AddGizmoListener {

    private JSlider slideX, slideY;
    private int MIN = -50, MAX = 50, START = 0, SCALE = 10;
    private double ballSize = 0.5;

    public AddBallListener(iBuildGUI buildGui, IGizmoModel model, GizmoType type) {
        super(buildGui, model, type);
        slideX = createSlider();
        slideY = createSlider();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        double x = scaleValues(e.getX(), buildGui.getScaleX());
        double y = scaleValues(e.getY(), buildGui.getScaleY());
        if (SwingUtilities.isRightMouseButton(e)) {
            callPopup(e, x, y);
        } else {

            Instruction i = getInstruction(x - ballSize / 2, y - ballSize / 2);

            if (i != null) {
                model.executeInstruction(i);
            }
        }
    }

    @Override
    Instruction getInstruction(double x, double y) {
        try {
            return new DefaultInstructions.AddGizmo(type, type + "x" + (int) x + "y" + (int) y, new Location2D(x, y, 0.5, 0.5), new Vect(slideX.getValue() * 2, slideY.getValue() * 2));
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "That's not a valid location");
        }
        return new DefaultInstructions.DoNothing();
    }

    private JSlider createSlider() {
        JSlider gravity = new JSlider(JSlider.HORIZONTAL, MIN, MAX, START);
        gravity.setMajorTickSpacing(SCALE);
        gravity.setMinorTickSpacing(1);
        Hashtable<Integer, JLabel> table = new Hashtable<>();
        for (int i = MIN; i <= MAX; i += SCALE) {
            table.put(i, new JLabel("" + i / SCALE));
        }
        gravity.setLabelTable(table);
        gravity.setPaintLabels(true);
        gravity.setPaintTicks(true);
        Styliser.componentStyler(gravity);
        return gravity;
    }

    public JSlider getSlideX() {
        return slideX;
    }

    public JSlider getSlideY() {
        return slideY;
    }

    public double scaleValues(double position, double scale) {
        return (position / scale);
    }
}
