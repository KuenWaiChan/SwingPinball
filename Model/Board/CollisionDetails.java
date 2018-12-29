package Model.Board;

import Model.Instruction;
import Model.ProtoGizmo;
import Model.util.DefaultInstructions;
import physics.Vect;

import java.util.HashSet;

/**
 * @author Murray Wood Demonstration of MVC and MIT Physics Collisions 2014
 */

public class CollisionDetails {
    private double tuc;
    private Vect velo;
    private Vect velo2;
    private ProtoGizmo gizmo2;
    private ProtoGizmo gizmo;
    private boolean IS_MULTIPLE_MOVING_GIZMOS = false;

    public CollisionDetails(double t, Vect v) {
        tuc = t;
        velo = v;
    }

    public CollisionDetails(double t, Vect v, ProtoGizmo gizmo2, Vect velo2, boolean b) {
        IS_MULTIPLE_MOVING_GIZMOS = b;
        this.tuc = t;
        this.velo = v;
        this.velo2 = velo2;
        this.gizmo2 = gizmo2;

        if(v.length()>150 || velo2.length()>150)
            tuc=Double.POSITIVE_INFINITY;
    }

    public double getTuc() {
        return tuc;
    }


    public void setGizmo(ProtoGizmo gizmo) {
        this.gizmo = gizmo;
    }

    public void changeGizmoVelocity(double timeLeft, ConcreteGizmoModel reactionTarget) {
        if (gizmo == null || gizmo2 == null || reactionTarget == null || timeLeft < tuc)
            return;


        //System.out.println("velo of l" + velo.length() + ": " + velo + " velo2 of length " + velo2.length()+": " + velo2);
        if(velo.length()>150)
            return;
        gizmo.setVelocity(velo);

        if (IS_MULTIPLE_MOVING_GIZMOS) gizmo2.setVelocity(velo2);
        else if (gizmo.getCollisionReactionGenerator() instanceof DefaultInstructions.BallCollision)
            ((DefaultInstructions.BallCollision) gizmo.getCollisionReactionGenerator() ).setOtherGizmo(gizmo2);


        HashSet<Instruction> connections = new HashSet<>();

        if (gizmo != null && !(gizmo.getReactionGenerator() instanceof DefaultInstructions.FlipperMove))
            try {
                gizmo.getCollisionReaction().process(reactionTarget);
                extractInstructions(connections, gizmo);
            } catch (DefaultInstructions.BadInstructionException e) {
            }
        if (gizmo2 != null && !(gizmo2.getReactionGenerator() instanceof DefaultInstructions.FlipperMove))
            try {
                gizmo2.getCollisionReaction().process(reactionTarget);
                extractInstructions(connections, gizmo2);
            } catch (DefaultInstructions.BadInstructionException e) {
                //nah
            }


        extractInstructions(connections, gizmo);
        extractInstructions(connections, gizmo2);

        for (Instruction i : connections)
            try {
                i.process(reactionTarget);
            } catch (DefaultInstructions.BadInstructionException e) {
                //nah
            }

    }

    private void extractInstructions(HashSet<Instruction> connections, ProtoGizmo gizmo) {
        for (ProtoGizmo g : gizmo.getConnectedGizmos())
            if (g != null && g.getTriggerReaction() != null)
                connections.add(g.getTriggerReaction());
    }
}
