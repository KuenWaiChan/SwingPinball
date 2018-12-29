package Model;

import Model.Gizmo.GizmoProperties.IBoardPlacement;
import Model.Gizmo.GizmoProperties.IDrawingShape;
import Model.Gizmo.GizmoProperties.IPhysicsShape;
import Model.Gizmo.drawing2D.FlipperDrawer;
import Model.Gizmo.physics2D.PhysicsShape2D;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import physics.Vect;

import java.awt.*;
import java.util.HashSet;

import static Model.util.DefaultInstructions.Reaction;

public class ProtoGizmo {
    protected HashSet<ProtoGizmo> connectedGizmos = new HashSet<>();
    //Definition of a GridGizmo
    private String name = "OuterWalls";
    private GizmoType type = GizmoType.Wall;
    private boolean overlappable = false;
    private Reaction triggerAction = new DefaultInstructions.BumperCollision();
    private boolean movable = false;
    private boolean frozen = false;
    private IDrawingShape drawingShape;
    private IPhysicsShape physicsShape = null;
    private Vect velocity = new Vect(0, 0);
    private Color color = Color.red;
    private Color original = Color.red, alternate = Color.black;
    private double reflectionCoefficient = 1;
    private int flip = 0;
    private boolean hasDefaultColourScheme = true;
    private boolean rotates = false;
    private double angVel = Math.toRadians(1080);
    private Reaction collisionGenerator = new DefaultInstructions.DoNothingGenerator();

    //If Color and/or altColor are passed here then the color instruction is saved to file, not the default gizmo save
    public ProtoGizmo(String name, GizmoType type, IDrawingShape shape) {
        this(name, type, shape, null, null, null);

    }

    public ProtoGizmo(String name, GizmoType type, IDrawingShape drawingShape, IPhysicsShape physicsShape, Color color, Color altColor) {
        if (drawingShape == null)
            throw new NullPointerException("Drawing shape cannot be null");
        if (name == null)
            throw new NullPointerException("Name cannot be null");
        if (type == null)
            throw new NullPointerException("Type cannot be null");

        this.name = (name == null) ? "" : name;
        this.type = type;
        this.drawingShape = drawingShape;
        this.physicsShape = physicsShape;
        this.color = (color == null) ? Color.black : color;
        original = this.color;
        alternate = (altColor == null) ? Color.black : altColor;
    }

    public Instruction getCollisionReaction() {
        return collisionGenerator.generate(this);
    }

    public void setCollisionReaction(Reaction collisionGenerator) {
        this.collisionGenerator = collisionGenerator;
    }

    public Color getOriginalColor() {
        return original;
    }

    public Color getAlternateColor() {
        return alternate;
    }

    public void passTime(double time, GameConstraints constraints) {

        double xVel = velocity.x(),
                yVel = velocity.y(),
                newX = (xVel * time),
                newY = (yVel * time);
        switch (type) {
            case LeftFlipper:
            case RightFlipper:
                if (flip != 0) {
                    FlipperDrawer temp = (FlipperDrawer) drawingShape;
                    flip = temp.rotateFlipper(time, flip, constraints);
                    if (flip == 0) {
                        movable = false;
                        frozen = true;
                    }
                }
                break;
            case Ball:
                shift(newX, newY);
        }


    }

    public void shift(double x, double y) {
        drawingShape.move(x, y);
        if (physicsShape != null) physicsShape.move(x, y);
    }

    public boolean isOverlappable() {
        return overlappable;
    }

    public void setOverlappable(boolean overlappable) {
        this.overlappable = overlappable;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public IDrawingShape getDrawingShape() {
        return drawingShape;
    }

    public IPhysicsShape getPhysicsShape() {
        return (physicsShape != null) ? physicsShape : drawingShape.drawPhysics();
    }

    public IBoardPlacement getPhysicsLocation() {

        return (physicsShape != null) ? physicsShape.getLocation() : getDrawingLocation();
    }

    public IBoardPlacement getDrawingLocation() {
        return drawingShape.getLocation();
    }

    public void rotate() {
        if (this.isFrozen() || this.isOverlappable() || this.isMovable()) return;
        getDrawingShape().rotate();

        if (physicsShape != null) physicsShape.rotate(getDrawingLocation().r());
    }

    public ProtoGizmo copy() {
        ProtoGizmo clone = new ProtoGizmo(name, type, drawingShape.copy(), physicsShape, color, alternate);
        clone.setHasDefaultColourScheme(hasDefaultColourScheme);
        clone.setrotable(rotates);
        clone.setCollisionReaction(collisionGenerator);
        clone.setTriggerAction(triggerAction);
        clone.setReflectionCoefficient(reflectionCoefficient);
        clone.setOverlappable(overlappable);
        clone.setMovable(movable);
        clone.setFrozen(frozen);
        clone.setVelocity(new Vect(velocity.x(), velocity.y()));

        for (ProtoGizmo p : connectedGizmos) clone.addGizmo(p);

        return clone;
    }

    public Instruction getTriggerReaction() {
        return triggerAction.generate(this);
    }

    public GizmoType getType() {
        return type;
    }

    public String getName() {
        return name;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vect getVelocity() {
        return new Vect(velocity.x(), velocity.y());
    }

    public void setVelocity(Vect velocity) {
        this.velocity = velocity;
    }

    public double getReflectionCoefficient() {
        return reflectionCoefficient;
    }

    public void setReflectionCoefficient(double reflectionCoefficient) {
        this.reflectionCoefficient = reflectionCoefficient;
    }

    public HashSet<ProtoGizmo> getConnectedGizmos() {
        return connectedGizmos;
    }

    public void addGizmo(ProtoGizmo gizmo) {

        this.connectedGizmos.add(gizmo);
    }

    public void removeConnection(ProtoGizmo name) {
        this.connectedGizmos.remove(name);
    }

    public Reaction getReactionGenerator() {
        return triggerAction;
    }

    public void setFlip() {
        if (flip == 0) {
            movable = true;
            flip = 1;
            frozen = false;
        }

    }

    public void setAlternate(Color alternate) {
        this.alternate = alternate;
    }

    public void setOriginal(Color original) {
        this.original = original;
        this.color = original;
    }

    public void setHasDefaultColourScheme(boolean hasDefaultColourScheme) {
        this.hasDefaultColourScheme = hasDefaultColourScheme;
    }

    public boolean hasDefaultColourScheme() {
        return hasDefaultColourScheme;
    }

    public void setrotable(boolean rota) {
        rotates = rota;
    }

    public boolean isRotable() {
        return rotates;
    }


    public double getAngVelocity() {
        if ((flip == -1 && type == GizmoType.RightFlipper) || (flip == 1 && type == GizmoType.LeftFlipper))
            return -angVel;
        if ((flip == 1 && type == GizmoType.RightFlipper) || (flip == -1 && type == GizmoType.LeftFlipper))
            return angVel;
        else
            return 0;
    }

    public void setTriggerAction(Reaction triggerAction) {
        this.triggerAction = triggerAction;
    }

    public Reaction getCollisionReactionGenerator() {
        return collisionGenerator;
    }

    public Reaction getTriggerReactionGenerator() {
        return triggerAction;
    }

    // Beautiful equals method, amirite ae?
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProtoGizmo gizmo = (ProtoGizmo) o;

        if (overlappable != gizmo.overlappable)
            return false;
        if (movable != gizmo.movable)
            return false;
        if (frozen != gizmo.frozen)
            return false;
        if (Double.compare(gizmo.reflectionCoefficient, reflectionCoefficient) != 0)
            return false;
        if (flip != gizmo.flip)
            return false;
        if (hasDefaultColourScheme != gizmo.hasDefaultColourScheme)
            return false;
        if (rotates != gizmo.rotates)
            return false;
        if (Double.compare(gizmo.angVel, angVel) != 0)
            return false;
        if ((connectedGizmos == null || gizmo.connectedGizmos == null || !
                (connectedGizmos.size() == 0 && gizmo.connectedGizmos.size() == 0
                        || deepCheck(connectedGizmos, gizmo.connectedGizmos)))) {
            return false;
        }
        if (!name.equals(gizmo.name))
            return false;
        if (type != gizmo.type)
            return false;
        if (!triggerAction.getClass().getName().equals(gizmo.triggerAction.getClass().getName()))
            return false;
        if (drawingShape != null ? !drawingShape.equals(gizmo.drawingShape) : gizmo.drawingShape != null)
            return false;
        if (physicsShape != null ? !physicsShape.equals(gizmo.physicsShape) : gizmo.physicsShape != null)
            return false;
        if (velocity != null ? !velocity.equals(gizmo.velocity) : gizmo.velocity != null)
            return false;
        if (!collisionGenerator.getClass().getName().equals(gizmo.collisionGenerator.getClass().getName()))
            return false;
        return true;
    }

    private boolean deepCheck(HashSet<ProtoGizmo> connectedGizmos, HashSet<ProtoGizmo> connectedGizmos1) {
        boolean cont = true;

        //System.out.println(connectedGizmos.size() + " " + connectedGizmos1.size());
        for (ProtoGizmo c : connectedGizmos) {
            if (connectedGizmos1.add(c))
                return false;
        }

        return true;
    }

    public void clearConnections() {
        connectedGizmos = new HashSet<>();
    }
}

