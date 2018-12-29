package Model.util;

import Model.Board.ConcreteGizmoModel;
import Model.Gizmo.GizmoProperties.IBoardPlacement;
import Model.Gizmo.GizmoProperties.IPhysicsShape;
import Model.Gizmo.drawing2D.*;
import Model.Instruction;
import Model.ProtoGizmo;
import physics.Vect;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class DefaultInstructions {


    private static boolean checkedMove(ConcreteGizmoModel model, ProtoGizmo target, double x, double y) {
        if (target == null)
            return false;
        target.shift(x, y);
        for (ProtoGizmo p : model.getGizmos().values())
            if (!p.equals(target) && target.getPhysicsLocation().contains(p.getPhysicsLocation())) {
                target.shift(-x, -y);
                return false;
            }
        return true;
    }


    public interface Reaction {
        Instruction generate(ProtoGizmo affectedGizmo);
    }

    public static class SetTimeMultipler implements Instruction {

        double mult;

        public SetTimeMultipler(double mult) {
            this.mult = mult;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) throws BadInstructionException {
            concreteModel.getConstraints().setTimeMultiplier(mult);
        }
    }

    public static class DoNothing implements Instruction {

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            ////////System.out.println.out.println(this.getClass() + " " + "was executed.");
            //Guess what? Do nothing.
        }
    }

    public static class SetFriction implements Instruction {
        private double mu1, mu2;

        public SetFriction(double mu1, double mu2) {
            this.mu1 = mu1;
            this.mu2 = mu2;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            concreteModel.getConstraints().setFriction(mu1, mu2);
        }
    }

    public static class SetGravity implements Instruction {
        private final double g;

        public SetGravity(double g) {
            this.g = g;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
//            //////System.out.println.out.println(this.getClass() + " " + "was executed.");
            concreteModel.getConstraints().setGravity(g);
        }
    }

    public static class SetWind implements Instruction {
        private final double w;

        public SetWind(double w) {
            this.w = w;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            concreteModel.getConstraints().setWind(w);
        }
    }

    public static class SetAbsorptionRate implements Instruction {
        private final double a;

        public SetAbsorptionRate(double a) {
            this.a = a;
        }

        @Override
        public void process(ConcreteGizmoModel concreteGizmoModel) {
            concreteGizmoModel.getConstraints().setAbsorption(a);
        }

    }

    /**
     * The following Instructions affect Gizmos
     */
    public static class Rotate implements Instruction {
        private String p;

        public Rotate(String g) {
            p = g;
        }


        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            ProtoGizmo proto = concreteModel.getGizmo(p);
            if (proto != null)
                proto.rotate();
        }
    }

    /**
     * Adds (x,y) to an object's location, shifting it along.
     */
    public static class Shift implements Instruction {
        double x, y;
        String name;

        public Shift(double x, double y, String name) {
            this.x = x;
            this.y = y;
            this.name = name;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {

            ProtoGizmo proto = concreteModel.getGizmo(name);
            double sx = x - proto.getPhysicsLocation().x(),
                    sy = y - proto.getPhysicsLocation().y();
            checkedMove(concreteModel, proto, sx, sy);
        }
    }

    /**
     * Freezes a gizmo, preventing it from moving or interacting
     */
    public static class Freeze implements Instruction {
        private ProtoGizmo p;

        public Freeze(ProtoGizmo g) {
            p = g;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            p.setFrozen(true);
        }
    }

    /**
     * Restores a gizmo - it can now move and interact, if it could before.
     */
    public static class Thaw implements Instruction {
        private ProtoGizmo p;

        public Thaw(ProtoGizmo g) {
            p = g;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            p.setFrozen(false);
        }
    }


    /**
     * /**
     * Image rotation algorithm:
     * <p>
     * Image img = ImageIO.read(this.getClass().getResource("img/unorientatedFlipper.png"));
     * int rotationDegrees, x,y,x2,y2
     * double centX (img.width/2 + x)
     * double centY y2-img.getHeight()/2
     * <p>
     * AffineTransform tr=AffineTransform.getScaleInstance(1,1);
     * tr.rotate(Math.toRadians(rotationDegrees), centreOfRotationX, centreOfRotationY)
     * tr.translate(x,y)
     * <p>
     * buffer.drawImage(img,tr,null);
     */

    public static class ColorChange implements Instruction {

        private static final int WAIT_TIME = 1000; //1 second
        boolean change = true;
        private ProtoGizmo p;
        private Timer colChange;

        ColorChange(ProtoGizmo p) {
            this.p = p;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            p.setColor(p.getAlternateColor());

            //after 1 second, revert color;
            colChange = new Timer(0, e -> {
                if (change)
                    p.setColor(p.getOriginalColor());
            });

            colChange.setRepeats(false);
            colChange.setInitialDelay(WAIT_TIME);
            colChange.start();

        }

        public void setColorToOriginal() {
            change = false;
        }

        public Timer getTimer() {
            return colChange;
        }
    }

    public static class AddGizmo implements Instruction {
        ProtoGizmo p;
        String name;
        String opcode;
        Vect v;
        IBoardPlacement l;
        boolean defaultcol=true;
        Color original, alternate;

        public AddGizmo(GizmoType type, String name, IBoardPlacement loc, Vect vel, Color original, Color alternate) {
            this(type, name, loc, vel);
            this.original = original;
            this.alternate = alternate;
            this.defaultcol=!(original==null && alternate==null);
        }

        public AddGizmo(GizmoType type, String name, IBoardPlacement loc, Vect vel) {
            p = null;    //By default, a grid element


            this.name = name;
            this.opcode = type.name();
            this.v = vel;
            this.l = loc;

            assert type != GizmoType.Wall : "Supplied GizmoType is not supported";


            DrawingShape2D drawer = null;
            IPhysicsShape physicsShape = null;
            boolean movable = false, rotable = false, frozen = false, overlappable = false;
            Vect velocity = new Vect(v.x(), v.y());
            Reaction collisionReaction = new DoNothingGenerator();
            Reaction triggerAction = new DoNothingGenerator();
            double reflectionCoefficient = 1.0;

            switch (type) {
                case Square:
                    drawer = new RectangleDrawer(loc.w(), loc.h(), loc.x(), loc.y(), 0);
                    collisionReaction = new BumperCollision();
                    triggerAction = collisionReaction;
                    original = new Color(0xC91F37);
                    alternate = new Color(0x6B9362);
                    break;
                case Circle:
                    drawer = new CircleDrawer(0.5, loc.x(), loc.y());
                    collisionReaction = new BumperCollision();
                    triggerAction = collisionReaction;
                    original = new Color(0x006442);
                    alternate = new Color(0xF3C13A);
                    break;
                case Triangle:
                    collisionReaction = new BumperCollision();
                    triggerAction = collisionReaction;
                    drawer = new TriangleDrawer(loc.w(), loc.h(), loc.x(), loc.y(), 0);
                    original = new Color(0x4D8FAC);
                    alternate = new Color(0x22A7F0);
                    break;
                case Absorber:
                    drawer = new RectangleDrawer(loc.w(), loc.h(), loc.x(), loc.y(), 0);
                    triggerAction = new BumperCollision();
                    collisionReaction = triggerAction;
                    original = new Color(0x8D608C);
                    alternate = new Color(0x763568);
                    break;
                case Ball:
                    movable = true;
                    drawer = new CircleDrawer(0.25, loc.x(), loc.y());
                    triggerAction = new BumperCollision();
                    collisionReaction = new BallCollision();
                    original = new Color(0x757D75);
                    alternate = new Color(0x6C7A89);
                    break;
                case RightFlipper:
                    rotable = true;
                    drawer = new FlipperDrawer(2, 2, loc.x(), loc.y(), 0, true);
                    triggerAction = new FlipperAction();
                    reflectionCoefficient = 0.95;
                    original = new Color(0xF7CA18);
                    alternate = new Color(0xE08A1E);
                    break;
                case LeftFlipper:
                    drawer = new FlipperDrawer(2, 2, loc.x(), loc.y(), 0, false);
                    rotable = true;
                    triggerAction = new FlipperAction();
                    reflectionCoefficient = 0.95;
                    original = new Color(0xF7CA18);
                    alternate = new Color(0xE08A1E);
                    break;

            }


            p = new ProtoGizmo(name, type, drawer, physicsShape, original, alternate);
            p.setrotable(rotable);
            p.setCollisionReaction(collisionReaction);
            p.setTriggerAction(triggerAction);
            p.setReflectionCoefficient(reflectionCoefficient);
            p.setOverlappable(overlappable);
            p.setMovable(movable);
            p.setFrozen(frozen);
            p.setVelocity(velocity);
        }

        @Override
        public synchronized void process(ConcreteGizmoModel concreteModel) throws BadInstructionException {

            //Reserved name
            if (name == null || p == null || this.name.equals("Outer Walls") || concreteModel.getGizmo(this.name) != null) {
                return;
            }
            //Check if occupied space

            if (this.original != null) {
                p.setOriginal(original);
                p.setHasDefaultColourScheme(defaultcol);
            }
            if (this.alternate != null) {
                p.setAlternate(alternate);

                p.setHasDefaultColourScheme(defaultcol);
            }
            if (!p.isOverlappable()) {
                IBoardPlacement phys = p.getPhysicsLocation();

                for (ProtoGizmo gizmo : (concreteModel.getGizmos().values())) {
                    if (phys.contains(gizmo.getPhysicsLocation()))
                        throw new BadInstructionException("That space is occupied by " + gizmo.getName());
                }
            }
            concreteModel.getGizmos().put(name, p);
        }

    }

    public static class BadInstructionException extends Exception {
        public BadInstructionException(String s) {
            super(s);
        }
    }

    public static class FlipperMove implements Instruction {

        ProtoGizmo gizmo;

        public FlipperMove(ProtoGizmo gizmo) {
            this.gizmo = gizmo;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            switch (gizmo.getType()) {
                case RightFlipper: //fallthrough
                case LeftFlipper:
                    gizmo.setFlip();
            }

        }
    }

    public static class RemoveGizmo implements Instruction {
        String name;

        public RemoveGizmo(String name) {
            this.name = name;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
//            //////System.out.println.out.println(this.getClass() + " " + "was executed.");

            concreteModel.removeGizmo(name);
        }
    }

    public static class Connect implements Instruction {
        String g1, g2;

        public Connect(String gizmo1, String gizmo2) {
            g1 = gizmo1;
            g2 = gizmo2;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            ProtoGizmo
                    p1 = concreteModel.getGizmo(g1),
                    p2 = concreteModel.getGizmo(g2);

            if (p1 == null || p2 == null) return;


            p1.addGizmo(p2);
        }

        @Override
        public String toString() {
            return "Connect" + g1 + " " + " " + g2;
        }
    }

    public static class Disconnect implements Instruction {
        String g1, g2;

        public Disconnect(String gizmo1, String gizmo2) {
            g1 = gizmo1;
            g2 = gizmo2;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            ProtoGizmo
                    p1 = concreteModel.getGizmo(g1),
                    p2 = concreteModel.getGizmo(g2);

            if (p1 == null || p2 == null) return;

            p1.removeConnection(p2);
        }

        @Override
        public String toString() {
            return "Disconnect" + g1 + " " + " " + g2;
        }
    }

    public static class KeyConnect implements Instruction {
        Integer key;
        String dir, name;

        public KeyConnect(Integer key, String dir, String name) {
            this.key = key;
            this.dir = dir;
            this.name = name;
            ////////System.out.println.out.println("ni to al as [" + key + "} fur die [" + dir + "] auf [" + name + "] in dem MODEL");
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            ProtoGizmo p = concreteModel.getGizmo(name);

            if (p != null)
                concreteModel.addKeyStroke(dir.equals("up"), key, p);

        }

        @Override
        public String toString() {
            return "KeyConnect key " + key + " " + dir + " " + name;
        }
    }

    public static class DoNothingGenerator implements Reaction {

        @Override
        public Instruction generate(ProtoGizmo affectedGizmo) {
            return new DoNothing();
        }
    }

    public static class BumperCollision implements Reaction {

        ColorChange c = null;

        @Override
        public synchronized Instruction generate(ProtoGizmo affectedGizmo) {
            if (c != null && c.getTimer() != null && c.getTimer().isRunning()) {
                c.setColorToOriginal();
            }
            if (affectedGizmo == null)
                return new DoNothing();
            else
                return (c = new ColorChange(affectedGizmo));

        }
    }


    private static class FlipperAction implements Reaction {

        @Override
        public Instruction generate(ProtoGizmo affectedGizmo) {
            //////System.out.println.out.println("gen act");
            return new FlipperMove(affectedGizmo);
        }

    }


    private static class AbsorberAction implements Reaction {
        private ProtoGizmo ball = null;

        @Override
        public Instruction generate(ProtoGizmo affectedGizmo) {
            //Do some gizmamo stuff;
            return concreteModel -> {
                if (ball != null) {
                    //Do ejection
                    if (checkedMove(concreteModel, ball, 0, -1 - affectedGizmo.getPhysicsLocation().h())) {
                        ball.setFrozen(false);
                        ball.setVelocity(new Vect(0, -50));

                        ball = null;
                    }
                }
            };
        }

        public boolean setBall(ProtoGizmo ball) {
            if (this.ball != null)
                return false;
            this.ball = ball;
            return true;
        }
    }

    public static class BallCollision implements Reaction {
        ProtoGizmo otherGizmo;

        public void setOtherGizmo(ProtoGizmo other) {
            otherGizmo = other;
        }

        @Override
        public Instruction generate(ProtoGizmo affectedGizmo) {

            if (otherGizmo != null && otherGizmo.getType() == GizmoType.Absorber && affectedGizmo != null) {
                otherGizmo.setCollisionReaction(new BumperCollision());
                AbsorberAction a = new AbsorberAction();
                a.setBall(affectedGizmo);
                otherGizmo.setTriggerAction(a);

                affectedGizmo.setFrozen(true);
                affectedGizmo.setVelocity(new Vect(0, 50));

                IBoardPlacement physLoc = otherGizmo.getPhysicsLocation();
                affectedGizmo.shift(
                        physLoc.x() + physLoc.w() - affectedGizmo.getPhysicsLocation().x() - affectedGizmo.getPhysicsLocation().w() - 0.25,
                        physLoc.y() + physLoc.h() - affectedGizmo.getPhysicsLocation().y() - affectedGizmo.getPhysicsLocation().h() - 0.25
                );
            }
            otherGizmo = null;
            return new DoNothing();
        }
    }

    public static class ChangeGizmoVelocity implements Instruction {
        private String name;
        private double vX;
        private double vY;

        public ChangeGizmoVelocity(String name, double vX, double vY) {
            this.name = name;
            this.vX = vX;
            this.vY = vY;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            HashMap<String, ProtoGizmo> gizmos = concreteModel.getGizmos();
            ProtoGizmo p = gizmos.get(name);
            if (p != null)
                p.setVelocity(new Vect(vX, vY));
        }
    }

    public static class GeneralVelocity implements Instruction {
        private Vect vector;

        public GeneralVelocity(double vX, double vY) {
            vector = new Vect(vX, vY);
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) {
            HashMap<String, ProtoGizmo> gizmos = concreteModel.getGizmos();
            for (ProtoGizmo p : gizmos.values())
                if (p.isMovable())
                    p.setVelocity(p.getVelocity().plus(vector));

        }
    }

    public static class addBallToAbsorber implements Instruction {
        private String absorber;
        private String ball;

        public addBallToAbsorber(String absorber, String ball) {
            this.absorber = absorber;
            this.ball = ball;
        }

        @Override
        public void process(ConcreteGizmoModel concreteModel) throws BadInstructionException {
            if (absorber == null || ball == null)
                return;
            ProtoGizmo bal = concreteModel.getGizmo(ball),
                    absorba = concreteModel.getGizmo(absorber);
            bal.getCollisionReaction().process(concreteModel);

            if (absorba.getTriggerReactionGenerator() instanceof AbsorberAction)
                return;

            AbsorberAction a = new AbsorberAction();
            a.setBall(bal);
            absorba.setCollisionReaction(a);

            bal.setFrozen(true);
            bal.setVelocity(new Vect(0, 50));
            IBoardPlacement physLoc = absorba.getPhysicsLocation();
            bal.shift(
                    physLoc.x() + physLoc.w() - bal.getPhysicsLocation().x() - bal.getPhysicsLocation().w() - 0.25,
                    physLoc.y() + physLoc.h() - bal.getPhysicsLocation().y() - bal.getPhysicsLocation().h() - 0.25
            );
        }
    }
}

//    }

