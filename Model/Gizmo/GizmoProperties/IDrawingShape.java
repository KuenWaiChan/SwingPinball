package Model.Gizmo.GizmoProperties;

import Model.Gizmo.physics2D.PhysicsShape2D;
import Controller.DrawController.iDrawer;

import java.awt.*;

public interface IDrawingShape {
    void draw(iDrawer canvasPainter, Color c);
    PhysicsShape2D drawPhysics();

    IDrawingShape copy();

    void move(double x, double y);

    void rotate();
    IBoardPlacement getLocation();
 
}
