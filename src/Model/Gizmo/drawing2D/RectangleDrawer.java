package Model.Gizmo.drawing2D;

import Controller.DrawController.iDrawer;
import Model.Gizmo.GizmoProperties.IDrawingShape;
import Model.Gizmo.physics2D.Location2D;
import Model.Gizmo.physics2D.PhysicsShape2D;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;

import java.awt.*;
import java.util.Arrays;

public class RectangleDrawer extends DrawingShape2D {

    public RectangleDrawer(double w, double h, double posx, double posy, int rotation) {
        super(new Location2D(posx, posy, w, h), rotation, "Rectangle");
    }

    @Override
    public void draw(iDrawer canvasPainter, Color c) {
        super.draw(canvasPainter, c);
        old = canvasPainter.drawRectangle(location.x(), location.y(), location.w(), location.h(), c);
    }


    @Override
    protected void drawPhysicsHere() {
        double w = location.w();
        double h = location.h();

        double posx = location.x();
        double posy = location.y();

        Circle[] circ = {
                (new Circle(new Vect(posx, posy), 0.0)),
                new Circle(new Vect(w + posx, posy), 0.0),
                new Circle(new Vect(posx, h + posy), 0.0),
                new Circle(new Vect(w + posx, h + posy), 0.0)
        };

        LineSegment[] lin = {new LineSegment(new Vect(posx, posy), new Vect(posx, posy + h)),
                new LineSegment(new Vect(posx, posy), new Vect(posx + w, posy)),
                new LineSegment(new Vect(w + posx, h + posy), new Vect(posx, posy + h)),
                new LineSegment(new Vect(w + posx, h + posy), new Vect(posx + w, posy))};

        super.physicsShape = new PhysicsShape2D(Arrays.asList(lin), Arrays.asList(circ), location);

    }

    @Override
    public IDrawingShape copy() {
        return new RectangleDrawer(location.w(), location.h(), location.x(), location.y(), location.r());
    }
}

