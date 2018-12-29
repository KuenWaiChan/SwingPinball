package Model.Gizmo.drawing2D;

import Controller.DrawController.iDrawer;
import Model.Gizmo.GizmoProperties.IDrawingShape;
import Model.Gizmo.physics2D.Location2D;
import Model.Gizmo.physics2D.PhysicsShape2D;
import javafx.util.Pair;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriangleDrawer extends DrawingShape2D {
    public TriangleDrawer(double w, double h, double x, double y, int rot) {
        super(new Location2D(x, y, w, h), rot, "Triangle");
    }

    @Override
    public void draw(iDrawer canvasPainter, Color col) {
        super.draw(canvasPainter, col);
        List<Pair<Double, Double>> points = new ArrayList<>();

        for (Circle c : physicsShape.getCircles()) points.add(new Pair<>(c.getCenter().x(), c.getCenter().y()));

        old = canvasPainter.drawPolygon(points, col);

    }

    @Override
    protected void drawPhysicsHere() {
        double posx = location.x();
        double posy = location.y();

        Circle[] circ = {(new Circle(new Vect(posx, posy), 0.0)),
                (new Circle(new Vect(posx + 1, posy), 0.0)),
                (new Circle(new Vect(posx, 1 + posy), 0.0))};

        LineSegment[] lin = {new LineSegment(new Vect(posx, posy), new Vect(posx + 1, posy)),
                new LineSegment(new Vect(posx, posy), new Vect(posx, posy + 1)),
                new LineSegment(new Vect(posx, 1 + posy), new Vect(posx + 1, posy))};

        super.physicsShape = new PhysicsShape2D(Arrays.asList(lin), Arrays.asList(circ), location);
    }

    @Override
    public IDrawingShape copy() {
        return new TriangleDrawer(location.w(), location.h(), location.x(), location.y(), location.r());
    }

}
