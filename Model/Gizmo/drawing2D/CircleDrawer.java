package Model.Gizmo.drawing2D;


import Controller.DrawController.iDrawer;
import Model.Gizmo.GizmoProperties.IDrawingShape;
import Model.Gizmo.physics2D.Location2D;
import Model.Gizmo.physics2D.PhysicsShape2D;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;

import java.awt.*;
import java.util.ArrayList;

public class CircleDrawer extends DrawingShape2D {
    double r;

    public CircleDrawer(double r, double x, double y) {
        super(new Location2D(x, y, r * 2,  r * 2), 0, "Circle");
         this.r = r;
    }


    @Override
    public void draw(iDrawer canvasPainter, Color c) {
        super.draw(canvasPainter, c);
        old = canvasPainter.drawCircle(location.x(), location.y(), r, c);
    }

    @Override
    public PhysicsShape2D drawPhysics() {
        drawPhysicsHere();
        return physicsShape;
    }

    @Override
    protected void drawPhysicsHere() {
        ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
        ArrayList<Circle> circles = new ArrayList<Circle>();
        circles.add(new Circle(new Vect(location.x() + r, location.y() + r), r));
        super.physicsShape = new PhysicsShape2D(lines, circles, location);
    }

    @Override
    public IDrawingShape copy() {
        return new CircleDrawer(this.r, location.x(), location.y());
    }


    @Override
    public void move(double x, double y) { try {
        super.location.shift(x, y);
    } catch (Exception e){

    }
        drawPhysicsHere();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
