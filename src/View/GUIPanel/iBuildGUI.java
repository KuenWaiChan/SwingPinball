package View.GUIPanel;


import Model.util.GizmoType;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public interface iBuildGUI {
	void setMouseClick(MouseListener addAbsorberGizmoListener);
	double getScaleX();
	double getScaleY();

    void popUpMenu(GizmoType type, MouseEvent e, double x, double y, String name);

    void refresh();
}
