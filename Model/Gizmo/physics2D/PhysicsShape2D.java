package Model.Gizmo.physics2D;

import Model.Gizmo.GizmoProperties.IBoardPlacement;
import Model.Gizmo.GizmoProperties.IPhysicsShape;
import physics.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhysicsShape2D implements IPhysicsShape {
    private final IBoardPlacement location;
    List<LineSegment> lines = new ArrayList<>();
    List<Circle> circles = new ArrayList<>();
    boolean precise = false;

    public PhysicsShape2D(List<LineSegment> lines, List<Circle> circles, IBoardPlacement p, boolean precise){

        setPrecisionRotation(precise);
        for (LineSegment l : lines) {
            this.lines.add(new LineSegment(l.toLine2D()));
        }
        for (Circle c : circles) {
            this.circles.add(new Circle(c.getCenter(), c.getRadius()));
        }
        this.location = p;
        if (!precise)
            this.rotate(p.r());
        else
            this.preciseRotate(p.r());
    }

    public PhysicsShape2D(List<LineSegment> lines, List<Circle> circles, IBoardPlacement p) {
        this(lines,circles,p,false);
    }
        public void setPrecisionRotation(boolean precisionRotation) {

        this.precise = precisionRotation;
    }

    private void preciseRotate(int degrees) {
        Vect centerOfRotation = location.getCenter();

        if (degrees == 0)   //No point evaluating nothing!
            return;


        List<LineSegment> newLines = new ArrayList<>();
        List<Circle> newCircles = new ArrayList<>();

        HashMap<Vect, Vect> pointMap = new HashMap();
        for (Circle c : circles) {
            //Top left corner of a circle (our rendering point) rotated aroound the center of our shape by degrees.
            Circle newCircle = Geometry.rotateAround(c, centerOfRotation, new Angle(Math.toRadians(degrees)));

            newCircles.add(newCircle);
            pointMap.put(c.getCenter(), newCircle.getCenter());
        }


        //Treat circles as end points of each line.
        //Map line segment endpoints according to the movements of the circles.

        for (LineSegment l : lines)
            newLines.add(new LineSegment(
                    (pointMap.get(l.p1()) == null) ? l.p1() : pointMap.get(l.p1()),
                    (pointMap.get(l.p2()) == null) ? l.p2() : pointMap.get(l.p2())));

        this.lines = newLines;
        this.circles = newCircles;
    }

    @Override
    public void rotate(int degrees, Vect centerOfRotation) {
        if (degrees == 0)   //No point evaluating nothing!
            return;


        List<LineSegment> newLines = new ArrayList<>();
        List<Circle> newCircles = new ArrayList<>();

        HashMap<Vect, Vect> pointMap = new HashMap();
        for (Circle c : circles) {
            //Top left corner of a circle (our rendering point) rotated aroound the center of our shape by degrees.
            Circle newCircle = Geometry.rotateAround(c, centerOfRotation, new Angle(Math.toRadians(degrees)));
            if (circles.size() == 3)
                newCircle = new Circle(Math.round(newCircle.getCenter().x()), Math.round(newCircle.getCenter().y()), newCircle.getRadius());
            newCircles.add(newCircle);
            pointMap.put(c.getCenter(), newCircle.getCenter());
        }


        //Treat circles as end points of each line.
        //Map line segment endpoints according to the movements of the circles.

        for (LineSegment l : lines)
            newLines.add(new LineSegment(
                    (pointMap.get(l.p1()) == null) ? l.p1() : pointMap.get(l.p1()),
                    (pointMap.get(l.p2()) == null) ? l.p2() : pointMap.get(l.p2())));

        this.lines = newLines;
        this.circles = newCircles;

    }

    @Override
    public void rotate(int degrees) {
        rotate(degrees, location.getCenter());
    }

    public void flip(boolean dir) {


    }

    public PhysicsShape2D copy() {
        return new PhysicsShape2D(lines, circles, location.copy());
    }

    @Override
    public boolean overlapsWith(IPhysicsShape physicsShape) {
        List<LineSegment> l2 = physicsShape.getLines();
        List<Circle> c2 = physicsShape.getCircles();

        boolean overlap;

        for (LineSegment l : l2) {

            for (LineSegment ol : lines) {
                overlap = ol.toLine2D().intersects(l.toLine2D().getBounds2D());

                if (overlap) {
                    //Check if they are actually lying ON each other.
                    if (boundsCheck(l, ol)) return true;
                }
            }
            for (Circle oc : circles) {
                if (oc.getRadius() == 0)
                    continue;
                overlap = oc.toEllipse2D().intersects(l.toLine2D().getBounds2D());

                if (overlap) { //Check if they are actually lying ON each other.
                    if (boundsCheck(oc, l)) return true;
                }
            }
        }
        for (Circle c : c2) {
            if (c.getRadius() == 0)
                continue;
            for (LineSegment ol : lines) {
                overlap = c.toEllipse2D().intersects(ol.toLine2D().getBounds2D());

                if (overlap) {
                    if (boundsCheck(c, ol)) return true;
                }
            }

            for (Circle oc : circles) {
                Point2D oCentre = oc.getCenter().toPoint2D();
                Point2D centre = c.getCenter().toPoint2D();

                double or = oc.getRadius();
                double r = c.getRadius();
                double dist = oCentre.distance(centre);

                if (or == 0)
                    continue;
                overlap = (dist - or) < 0 || (dist - r) < 0;
                if (overlap) {
                    if (boundsCheck(c, oc)) return true;
                } else {

                }

            }
        }
        return false;
    }


    /**---------------------------------------------------**/
    /**
     * This set of methods generates bounding boxes for the circles and lines and checks for overlap
     **/

    private boolean boundsCheck(Circle ol, Circle l) {
        return ol.toEllipse2D().getBounds2D().intersects(l.toEllipse2D().getBounds2D());
    }

    private boolean boundsCheck(Circle ol, LineSegment l) {
        return ol.toEllipse2D().getBounds2D().intersects(l.toLine2D().getBounds2D());
    }

    private boolean boundsCheck(LineSegment l, LineSegment ol) {
        return !l.toLine2D().intersects((ol.toLine2D().getBounds2D())) && l.toLine2D().contains(ol.toLine2D().getBounds2D());
    }

    /**
     * ---------------------------------------------------
     **/

    @Override
    public List<LineSegment> getLines() {
        return lines;
    }

    @Override
    public List<Circle> getCircles() {
        return circles;
    }

    @Override
    public void move(double x, double y) {
        location.shift(x, y);

        int i = -1;
        for (LineSegment l : lines) {
            i++;
            lines.set(i, new LineSegment(l.p1().x() + x, l.p1().y() + y,
                    l.p2().x() + x, l.p2().y() + y));
        }

        i = -1;
        for (Circle c : circles) {
            i++;
            circles.set(i, new Circle(c.getCenter().x() + x,
                    c.getCenter().y() + y,
                    c.getRadius()));
        }
    }

    @Override
    public IBoardPlacement getLocation() {
        return location;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhysicsShape2D that = (PhysicsShape2D) o;

        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        // Lines
        if (lines.size() != that.lines.size()) return false;
        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).equals(that.lines.get(i))) return false;
        }
        // Circles
        if (circles.size() != that.circles.size()) return false;
        for (int i = 0; i < circles.size(); i++) {
            if (!circles.get(i).equals(that.circles.get(i))) return false;
        }
        return true;
    }
    @Override
    public String toString(){
        String s = "\n\t\t\tLines:";
        for(LineSegment l: lines){
            s+="\n\t\t\t\t"+l.toString();
        }
        s+="\n\t\t\t" +
                "Circles";
        for(Circle c: circles){
            s+="\n\t\t\t\t"+c.toString();
        }


        return s;

    }
}
