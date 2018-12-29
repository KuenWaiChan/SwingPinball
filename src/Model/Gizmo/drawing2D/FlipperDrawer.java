package Model.Gizmo.drawing2D;

import Controller.DrawController.iDrawer;
import Model.GameConstraints;
import Model.Gizmo.GizmoProperties.IDrawingShape;
import Model.Gizmo.physics2D.Location2D;
import Model.Gizmo.physics2D.PhysicsShape2D;
import physics.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


public class FlipperDrawer extends DrawingShape2D {
    double flipperRotation = 0;
    ArrayList<Shape> shapes = new ArrayList();
    private double radius = 0.25;
    boolean isRight;
    private double ox;

    //defaults to left
    public FlipperDrawer(double w, double h, double posx, double posy, int r) {
        super(new Location2D(posx, posy, w, h), r, "Flipper");
    }

    public FlipperDrawer(double w, double h, double x, double y, int r, boolean rightflipper) {
        this(w, h, x, y, r);
        isRight = rightflipper;
        ox = x;
        drawPhysicsHere();
    }

    private Vect rotPoint(Vect rotMe, Vect rotAroundMe, double rotMount) {
        return Geometry.rotateAround(rotMe, rotAroundMe, new Angle(Math.toRadians(rotMount)));
    }

    @Override
    public void draw(iDrawer canvasPainter, Color col) {
        super.draw(canvasPainter, col);

        for (Shape s : shapes)
            canvasPainter.clearShape(s);


        Circle c1 = physicsShape.getCircles().get(0), c2 = physicsShape.getCircles().get(1);
        LineSegment l1 = physicsShape.getLines().get(0);
        LineSegment l2 = physicsShape.getLines().get(1);
        Shape[] arr = {
                canvasPainter.drawCircle(c1.getCenter().x()-.25, c1.getCenter().y()-.25, c1.getRadius(), col),
//                canvasPainter.drawLine(l1.p1(),l1.p2(), col),
//                canvasPainter.drawLine(l2.p1(),l2.p2(), col),
                canvasPainter.drawLine(c1.getCenter(), c2.getCenter(), col),
                canvasPainter.drawCircle(c2.getCenter().x()-.25, c2.getCenter().y()-.25, c2.getRadius(), col)
        };
        shapes = new ArrayList<>(Arrays.asList(arr));
    }

    public int rotateFlipper(double time, int dir, GameConstraints constraints) {
        int out = dir;
        int step = 1080;
        step = (isRight) ? step : -step ;

        double angle = (step/constraints.TICKS)*(time/constraints.TICK_SPEED);
        if(dir == -1)
            angle = -angle;
        if(flipperRotation + angle > 90){
            flipperRotation = 90;
            out = -1;
        } else if(flipperRotation + angle <-90){
            flipperRotation = -90;
            out = -1;
        }else{
            flipperRotation += angle;
        }

        if(isRight){
            if(flipperRotation<0){
                flipperRotation = 0;
                out = 0;
            }
        } else if(flipperRotation>0){
            flipperRotation = 0;
            out = 0;
        }



        drawPhysicsHere();
        return out;
    }

    @Override
    protected void drawPhysicsHere() {
        double posx;

        if(isRight)
            posx = location.x()+1.75;
        else
            posx = location.x()+.25;




        double posy = location.y()+.25;

        double posx2 = posx ,
                posy2 = posy + 1.5;

        Vect p1 = new Vect(posx, posy),
                p2 = new Vect(posx2, posy2);

        Circle source,
                dest;
        LineSegment line[] = new LineSegment[2];
        if (physicsShape != null){
            source = new Circle(p1, radius);
            dest = new Circle(p2, radius);
            p2 = rotPoint(dest.getCenter(), source.getCenter(), flipperRotation);
            source = new Circle(source.getCenter(), radius);
            dest = new Circle(p2, radius);
            Vect    p3 = new Vect(source.getCenter().x()-.25, source.getCenter().y()),
                    p4 = new Vect(source.getCenter().x()+.25, source.getCenter().y()),
                    p5 = new Vect(dest.getCenter().x()-.25, dest.getCenter().y()),
                    p6 = new Vect(dest.getCenter().x()+.25, dest.getCenter().y());
            p3 = rotPoint(p3, source.getCenter(), flipperRotation);
            p4 = rotPoint(p4, source.getCenter(), flipperRotation);
            p5 = rotPoint(p5, dest.getCenter(), flipperRotation);
            p6 = rotPoint(p6, dest.getCenter(), flipperRotation);
            line[0] = new LineSegment(p3, p5);
            line[1] = new LineSegment(p4, p6);
        }
        else{
            source = new Circle(p1, radius);
            dest = new Circle(p2, radius);
            line[0] = new LineSegment(new Vect(p1.x()-.25, p1.y()), new Vect(p1.x()-.25, p2.y()));
            line[1] = new LineSegment(new Vect(p1.x()+.25, p1.y()), new Vect(p1.x()+.25, p2.y()));
        }

        Circle[] circ = {source, dest, new Circle(line[0].p1(),0), new Circle(line[1].p1(),0), new Circle(line[0].p2(),0), new Circle(line[1].p2(),0)};


        physicsShape = new PhysicsShape2D(Arrays.asList(line), Arrays.asList(circ), location, true);


    }

    @Override
    public IDrawingShape copy() {
        return new FlipperDrawer(location.w(), location.h(), ox, location.y(), location.r(), isRight);
    }
}

