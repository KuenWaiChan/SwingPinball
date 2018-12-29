package Model.Gizmo.drawing2D;

import Controller.DrawController.iDrawer;
import Model.Gizmo.GizmoProperties.IBoardPlacement;
import Model.Gizmo.GizmoProperties.IDrawingShape;
import Model.Gizmo.physics2D.PhysicsShape2D;

import java.awt.*;

public abstract class DrawingShape2D implements IDrawingShape {
    IBoardPlacement location;
    PhysicsShape2D physicsShape;
    Shape old;
    String type = "Default";

    public DrawingShape2D(IBoardPlacement location, int rot, String type) {
        this.location = location;
        location.rotateBy(rot);
        drawPhysicsHere();
        this.type=type;

    }

    @Override
    public String toString(){
        return "\n\t\t\tDrawing Location:"+location.toString()+"\n\t\t\tInternal Physics Shape:"+physicsShape.toString()+ "\n\t\t\t"+"Type: " + type;
    }

    @Override
    public void draw(iDrawer canvasPainter, Color c) {

        assert c != null : "A shape can't paint with no colour";
        assert canvasPainter != null : "A shape can't paint on no canvas";
        if (physicsShape == null)
            drawPhysicsHere();

        if (old != null) canvasPainter.clearShape(old);

    }

    @Override
    public IBoardPlacement getLocation() {
        return location;
    }

    @Override
    public void move(double x, double y) {
        location.shift(x, y);
        drawPhysicsHere();
    }

    @Override
    public void rotate() {
        location.rotateBy(90);
        drawPhysicsHere();

    }

    @Override
    public PhysicsShape2D drawPhysics() {
        return physicsShape;
    }

    protected abstract void drawPhysicsHere();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrawingShape2D that = (DrawingShape2D) o;

        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (physicsShape != null ? !physicsShape.equals(that.physicsShape) : that.physicsShape != null) return false;
        return old != null ? old.equals(that.old) : that.old == null;
    }
}
