package Model.Gizmo.GizmoProperties;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;

import java.util.List;

public interface IPhysicsShape {
    void rotate(int degrees, Vect centerOfRotation);

    void rotate(int degrees);

    IPhysicsShape copy();

    boolean overlapsWith(IPhysicsShape physicsShape);

    List<LineSegment> getLines();

    List<Circle> getCircles();

    void move (double x, double y);
    void flip (boolean dir);
    IBoardPlacement getLocation();
}
