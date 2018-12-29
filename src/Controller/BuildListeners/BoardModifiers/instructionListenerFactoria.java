package Controller.BuildListeners.BoardModifiers;

import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import physics.Vect;

public class instructionListenerFactoria
{
	private static  IGizmoModel MODEL;

	public static void setModel(IGizmoModel model){
		MODEL = model;
	}
	/**
	 * makes the model call add gizmo
	 * If snapping to grid then x and y should be floored
	 *
	 * @param type
	 * @param x
	 * @param y
	 */
	public static void addGizmo(GizmoType type, double x, double y)
	{
		addGizmo(type, x, y, 1, 1);
	}

	public static void addGizmo(GizmoType type, double x, double y, double w, double h)
	{
		MODEL.executeInstruction(new DefaultInstructions.AddGizmo(type, type + "x" + x + "y" + y, new Location2D(x, y, w, h), Vect.ZERO));
	}

	public static void addMovingGizmo(GizmoType type, double x, double y, double w, double h, double vx, double vy)
	{
		MODEL.executeInstruction(new DefaultInstructions.AddGizmo(type, type + "x" + x + "y" + y, new Location2D(x, y, w, h), new Vect(vx, vy)));
	}

	public static void connectGizmos(double x1, double y1, double x2, double y2)
	{
		connectGizmos(MODEL.getName(x1,y1), MODEL.getName(x2,y2));
	}

	public static void connectGizmos(String a, String b)
	{
		if (a != null && b != null)
		MODEL.executeInstruction(new DefaultInstructions.Connect(a, b));
	}

	public static void disconnectGizmos(String a, String b)
	{
		if (a != null && b != null)
			MODEL.executeInstruction(new DefaultInstructions.Disconnect(a, b));
	}




	//TODO: ContextualDeleteListener possible?

	public static void deleteGizmo (double x, double y)
	{
		MODEL.executeInstruction( new DefaultInstructions.RemoveGizmo(MODEL.getName(x,y)));
	}

	public static void keyConnectGizmo (Integer keyStroke, String direction, double x, double y)
	{
		MODEL.executeInstruction(new DefaultInstructions.KeyConnect(keyStroke, direction, MODEL.getName(x,y)));
	}

	public static void moveGizmo (double x1, double y1, double x2, double y2) {
		MODEL.executeInstruction(new DefaultInstructions.Shift(x2,y2, MODEL.getName(x1,y1)));
	}

	public static void rotateGizmo (double x, double y)
	{
		MODEL.executeInstruction(new DefaultInstructions.Rotate(MODEL.getName(x,y)));
	}
}
