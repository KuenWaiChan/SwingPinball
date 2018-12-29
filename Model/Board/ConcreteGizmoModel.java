package Model.Board;

import Controller.DrawController.iDrawer;
import Model.GameConstraints;
import Model.Gizmo.GizmoProperties.IPhysicsShape;
import Model.Gizmo.drawing2D.RectangleDrawer;
import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.Instruction;
import Model.ProtoGizmo;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import javafx.util.Pair;
import physics.*;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * ConcreteGizmoModel class
 */

public class ConcreteGizmoModel extends Observable implements IGizmoModel {
    private FileParser parser = new FileParser();                              // BNF Parser to build gizmos from file
    private BoardWriter writer = new BoardWriter();                             // Creates a new file to generate board from given syntax
    private GameConstraints constraints = new GameConstraints();                    // Represents the set of Constraints like Width, Height, mu, mu2, etc
    private volatile HashMap<String, ProtoGizmo> gizmos = new HashMap<>();
    private ProtoGizmo walls;
    private ArrayList<String> errorMessages = new ArrayList<String>();
    private boolean readingFile;
    private boolean running = false;
    private HashMap<Integer, HashSet<ProtoGizmo>> keyUp = new HashMap<>();
    private HashMap<Integer, HashSet<ProtoGizmo>> keyDown = new HashMap<>();

    /**
     * ConcreteGizmoModel constructor with custom gizmos and game constaints
     */
    public ConcreteGizmoModel() {
        makeWalls();
    }


    /**
     * ConcreteGizmoModel constructor that creates empty model with default constraints
     *
     * @param constraints
     * @param gizmos
     * @param keyUp
     * @param keyDown
     */
    public ConcreteGizmoModel(GameConstraints constraints, HashMap<String, ProtoGizmo> gizmos, HashMap<Integer, HashSet<ProtoGizmo>> keyUp, HashMap<Integer, HashSet<ProtoGizmo>> keyDown) {
        this.constraints = (constraints != null) ? constraints : new GameConstraints();
        HashSet<Pair<String, HashSet<String>>> oldConnections = new HashSet<>();

        //clone triggered keys
        for (String gizmoName : gizmos.keySet()) {
            HashSet<String> replaceConnectionsSet = new HashSet<>();


            for (ProtoGizmo replaceMe : gizmos.get(gizmoName).getConnectedGizmos()) {
                if (replaceMe == null || replaceMe.getName() == null) continue;
                else replaceConnectionsSet.add(replaceMe.getName());
            }

            oldConnections.add(new Pair<String, HashSet<String>>(gizmoName, replaceConnectionsSet));
            this.gizmos.put(gizmoName, gizmos.get(gizmoName));
        }

        for (ProtoGizmo g : this.gizmos.values())
            g.clearConnections();

        for (Pair<String, HashSet<String>> oldConnection : oldConnections) {
            ProtoGizmo source = getGizmo(oldConnection.getKey());
            for (String connectionName : oldConnection.getValue()) {
                boolean found = false;
                for (ProtoGizmo connectedGiz : source.getConnectedGizmos())
                    if (connectedGiz.getName().equals(connectionName)) {
                        found = true;
                        break;
                    }
                if (!found)
                    source.addGizmo(getGizmo(connectionName));
            }
        }

        makeWalls();

        this.keyUp = new HashMap<>();
        this.keyDown = new HashMap<>();

        if (keyUp != null) {
            for (Integer i : keyUp.keySet()) {
                HashSet<ProtoGizmo> hs = new HashSet<>();
                for (ProtoGizmo p : keyUp.get(i))
                    hs.add(this.gizmos.get(p.getName()));
                this.keyUp.put(i, hs);
            }
        }

        if (keyDown != null)
            for (Integer i : keyDown.keySet()) {
                HashSet<ProtoGizmo> hs = new HashSet<>();
                for (ProtoGizmo p : keyDown.get(i))
                    hs.add(this.gizmos.get(p.getName()));
                this.keyDown.put(i, hs);
            }

    }


    @Override
    public GizmoType getType(double x, double y) {
        ProtoGizmo g = getGizmo(getName(x, y));
        return (g == null) ? null : g.getType();
    }

    /**
     * This method accepts a painter that acts as a controller for writing to the front end.
     * Used to update the front end.
     *
     * @param canvasPainter Draws to this painter.
     */
    @Override
    public synchronized void draw(iDrawer canvasPainter) {
        canvasPainter.clear();

        for (ProtoGizmo giz : gizmos.values())
            if (!giz.isOverlappable())
                giz.getDrawingShape().draw(canvasPainter, giz.getColor());

    }

    /**
     * Gives an instruction access to this gizmo model for execution.
     * It also updates all observers (Like the view) after the instruction.
     * <p>
     * Alternatively, use i.process(concreteModel) to avoid updating the observers.
     *
     * @param i The instruction to execute
     */
    @Override
    public synchronized void executeInstruction(Instruction i) {
        try {
            i.process(this);
        } catch (DefaultInstructions.BadInstructionException e) {
            this.errorMessages.add(e.getMessage());
        }

        if (!readingFile) updateEveryone();

    }

    /**
     * Tries to load the file at filename as a gizmo board. Does not crash on a badly formatted file.
     *
     * @param filename The file name and location to load
     * @throws FileNotFoundException If the file does not exist / couldn't be found.
     */
    @Override
    public synchronized void loadFile(String filename) throws FileNotFoundException {
        this.readingFile = true;
        this.gizmos = new HashMap();
        this.errorMessages = new ArrayList<>();
        this.constraints = new GameConstraints();
        parser.readFile(filename, this);
        makeWalls();
        this.readingFile = false;

        updateEveryone();
    }

    //If gizmos contains walls, remove from gizmos and set walls = to gizmos version
    //else create new walls
    private void makeWalls() {
        this.walls = null;
        for (String gizmo : gizmos.keySet()) {
            if (gizmo.equals("OuterWalls")) {
                this.walls = gizmos.get(gizmo).copy();
            }
        }

        if (this.walls != null) {
            gizmos.remove("OuterWalls");
        } else {
            //If walls couldn't be found, create a new set of walls.
            this.walls = new ProtoGizmo("OuterWalls", GizmoType.Wall, new RectangleDrawer(constraints.BOARD_WIDTH, constraints.BOARD_HEIGHT, 0, 0, 0), null, null, null);
            this.walls.setOverlappable(true);
        }
    }

    /**
     * Tries to write this board to the given file.
     *
     * @param file The location of the file to write to.
     */
    @Override
    public synchronized void writeFile(String file) {
        writer.writeFile(file, gizmos, constraints, keyDown, keyUp);
    }

    private synchronized void triggerKeys(Set<Integer> keysPressed, HashMap<Integer, HashSet<ProtoGizmo>> keysRegisteredForPress) {
        HashSet<ProtoGizmo> triggerMeList = new HashSet<>();             //We're creating a list so that we don't trigger the same gizmo twice in one request.

        for (Integer c : keysPressed) {
            if (!keysRegisteredForPress.containsKey(c)) continue;
            if (triggerMeList.size() == gizmos.size())
                break;                                                    //We've added every single gizmo...calm down and stop checking already

            if (!keysRegisteredForPress.containsKey(c)) continue;         //If not a key we want
            if (triggerMeList.size() == gizmos.size())
                break;                                                    //We've added every single gizmo...calm down and stop checking already
            triggerMeList.addAll(keysRegisteredForPress.get(c));
        }

        for (ProtoGizmo triggerSources : triggerMeList)
            triggerMeList.addAll(triggerSources.getConnectedGizmos());    //Get the gizmos triggered by those already found

        for (ProtoGizmo triggerSources : triggerMeList) {
            try {
                triggerSources.getTriggerReaction().process(this); //Execute their reactions in any old order
            } catch (DefaultInstructions.BadInstructionException e) {
                this.errorMessages.add(e.getMessage());
            }
        }


        for (ProtoGizmo triggerSources : triggerMeList) {
            try {
                triggerSources.getTriggerReaction().process(this); //Execute their reactions in any old order
            } catch (DefaultInstructions.BadInstructionException e) {
                this.errorMessages.add(e.getMessage());
            }
        }
        updateEveryone();                                               //Spread the word: "Good news everyone, we're changing!"

    }

    /**
     * Performs a complete tick of the game state - performs all collisions in order of occurence and passes the full time.
     * Tick length is defined by GameConstraints.TICK_SPEED.
     */
    @Override
    public synchronized void tick(Set<Integer> keyDown, Set<Integer> keyUp) {
        Set<ProtoGizmo> movingGizmosWithVelocity = new HashSet<>();
        Set<ProtoGizmo> rotableGizmos = new HashSet<>();

        if (keyDown != null) triggerKeys(keyDown, this.keyDown);
        if (keyUp != null) triggerKeys(keyUp, this.keyUp);


        for (ProtoGizmo gizmo : gizmos.values()) {
            if (gizmo != null) {
                if (gizmo.isMovable() && !gizmo.isFrozen()) {
                    movingGizmosWithVelocity.add(gizmo);

                } else {
                    rotableGizmos.add(gizmo);
                }
            }
        }


        for (ProtoGizmo g : movingGizmosWithVelocity)
            g.setVelocity(g.getVelocity()
                    .times((1 - constraints.getMu1() * constraints.TICK_SPEED - g.getVelocity().length() * constraints.getMu2() * constraints.TICK_SPEED))     //Friction multiplier
                    .plus(new Vect(Angle.DEG_90, constraints.getGravity() * constraints.TICK_SPEED))
                    .plus(new Vect(Angle.DEG_45, constraints.getWind() * constraints.TICK_SPEED)));                                                      //Gravity addition


        ////System.out.println(constraints.getGravity() + " " + constraints.getMu1() + " " + constraints.getMu2() + " " + constraints.getAbsorption());
        tick(constraints.TICK_SPEED, movingGizmosWithVelocity);

        updateEveryone();
    }

    /**
     * Advances the game state a tick recursively. Locates the next collision and executes it. Repeat until no time left.
     *
     * @param timeLeft     The milliseconds left in this tick.
     * @param movingGizmos The set of all moving gizmos that have not been evaluated for a collision yet.
     */
    private void tick(double timeLeft, Set<ProtoGizmo> movingGizmos) {
        if (timeLeft <= 0) return;

        Set<ProtoGizmo> checkedGizmos = new HashSet<>();

        CollisionDetails shortestDetails = new CollisionDetails(Double.POSITIVE_INFINITY, null);

        for (ProtoGizmo giz : movingGizmos) {
            checkedGizmos.add(giz);
            shortestDetails = min(shortestDetails, timeUntilCollision(giz, checkedGizmos), giz);

        }

        double timeToMoveFor = (shortestDetails.getTuc() < timeLeft) ? shortestDetails.getTuc() : timeLeft;

        for (ProtoGizmo giz : movingGizmos)
            giz.passTime(timeToMoveFor, constraints);

        shortestDetails.changeGizmoVelocity(timeToMoveFor, this);

        this.updateEveryone();
        tick(timeLeft - timeToMoveFor, movingGizmos);


    }

    /**
     * Calculates the next collision for p
     *
     * @param p             The ProtoGizmo to find the next collision for - if any exist.
     * @param checkedGizmos
     * @return
     */
    private CollisionDetails timeUntilCollision(ProtoGizmo p, Set<ProtoGizmo> checkedGizmos) {
        Set<ProtoGizmo> gizmosToCheck = new HashSet<>(gizmos.values());

//        gizmosToCheck.removeAll(checkedGizmos);
        gizmosToCheck.add(walls);

        CollisionDetails smallest = new CollisionDetails(Double.POSITIVE_INFINITY, null);
        if (!p.isMovable()) return smallest;
        for (ProtoGizmo g : gizmosToCheck)
            if (g.getType() == GizmoType.Ball || p.getType() == GizmoType.Ball)
                for (Circle item : p.getPhysicsShape().getCircles()) {
                    CollisionDetails existant = checkForCollisions(item, g, p.getVelocity(), (constraints.getAbsorption() * g.getReflectionCoefficient()));
                    smallest = (smallest == null) ? existant : min(smallest, existant, g);
                }


        return smallest;
    }

    private CollisionDetails collideMovers(ProtoGizmo gizmo, Vect oldVelocity, Circle item) {
        double time;
        CollisionDetails smallest = new CollisionDetails(Double.POSITIVE_INFINITY, null);
        double itemMass = Math.PI * (4 / 3) * (Math.pow(item.getRadius(), 3));
        for (Circle circle : gizmo.getPhysicsShape().getCircles()) {

            if ((time = Geometry.timeUntilBallBallCollision(item, oldVelocity, circle, gizmo.getVelocity())) < smallest.getTuc()) {
                double circleMass = Math.PI * (4 / 3) * (Math.pow(circle.getRadius(), 3));
                Geometry.VectPair velocities = Geometry.reflectBalls(item.getCenter(), itemMass, oldVelocity, circle.getCenter(), circleMass, gizmo.getVelocity());
                smallest = min(smallest, new CollisionDetails(time, velocities.v1, gizmo, velocities.v2, true), gizmo);
            }
        }
        return smallest;
    }

    /**
     * @param gizmo       Non-moving gizmo
     * @param oldVelocity Original velocity of the circle
     * @param item        The ball in motion
     * @param reflection  The reflection coefficient of impact
     * @return The closest collision for this ball
     */
    private CollisionDetails collideAgainstStationaryGizmo(ProtoGizmo gizmo, Vect oldVelocity, Circle item, double reflection) {
        CollisionDetails smallest = new CollisionDetails(Double.POSITIVE_INFINITY, null);
        double time;

        assert !gizmo.isMovable();

        IPhysicsShape gizmoShape = gizmo.getPhysicsShape();

        for (Circle circle : gizmoShape.getCircles())
            if ((time = Geometry.timeUntilCircleCollision(circle, item, oldVelocity)) < smallest.getTuc())
                smallest = min(smallest, new CollisionDetails(time, Geometry.reflectCircle(item.getCenter(), circle.getCenter(), oldVelocity, reflection), gizmo, gizmo.getVelocity(), false), gizmo);

        for (LineSegment line : gizmoShape.getLines())
            if ((time = Geometry.timeUntilWallCollision(line, item, oldVelocity)) < smallest.getTuc())
                smallest = min(smallest, new CollisionDetails(time, Geometry.reflectWall(line, oldVelocity, reflection), gizmo, gizmo.getVelocity(), false), gizmo);

        return smallest;
    }

    private CollisionDetails checkForCollisions(Circle item, ProtoGizmo gizmo, Vect oldVelocity, double reflection) {
        return (gizmo.isRotable()) ? collideAgainstRotatingGizmo(gizmo, oldVelocity, item, reflection, gizmo.getAngVelocity()) : (gizmo.isMovable()) ? collideMovers(gizmo, oldVelocity, item) : collideAgainstStationaryGizmo(gizmo, oldVelocity, item, reflection);
    }

    private CollisionDetails collideAgainstRotatingGizmo(ProtoGizmo gizmo, Vect oldVelocity, Circle item, double reflection, double angularVelocity) {
        CollisionDetails smallest = new CollisionDetails(Double.POSITIVE_INFINITY, null);
        double time;
        assert !gizmo.isMovable();

        IPhysicsShape gizmoShape = gizmo.getPhysicsShape();

        for (Circle circle : gizmoShape.getCircles())
            if ((time = Geometry.timeUntilRotatingCircleCollision(circle, circle.getCenter(), angularVelocity, item, oldVelocity)) < smallest.getTuc())
                smallest =min(smallest, new CollisionDetails(time, Geometry.reflectRotatingCircle(circle, circle.getCenter(), angularVelocity, item, oldVelocity), gizmo, gizmo.getVelocity(), false),gizmo);

        for (LineSegment line : gizmoShape.getLines())
            if ((time = Geometry.timeUntilRotatingWallCollision(line, line.p1(), angularVelocity, item, oldVelocity)) < smallest.getTuc())
                smallest = min(smallest, new CollisionDetails(time, Geometry.reflectRotatingWall(line, line.p1(), angularVelocity, item, oldVelocity, reflection), gizmo, gizmo.getVelocity(), false),gizmo);


        return smallest;
    }

    private CollisionDetails min(CollisionDetails a, CollisionDetails b, ProtoGizmo gizmoCollidingInB) {
        if (a.getTuc() > b.getTuc()) {
            b.setGizmo(gizmoCollidingInB);
            return b;
        } else
            return a;
    }

    /**
     * @return Deep clone of this gizmo model. Different gizmos too.
     */
    @Override
    public synchronized IGizmoModel copy() {
        HashMap<String, ProtoGizmo> gizmosCopy = new HashMap<>(gizmos);
        gizmosCopy.put("OuterWalls", walls);
        return new ConcreteGizmoModel(new GameConstraints(constraints), gizmosCopy, keyUp, keyDown);
    }

    /**
     * Get the name of whatever gizmo is at location l. Works from the drawing location rather than physics location.
     *
     * @return The name of the gizmo at location l.
     */
    @Override
    public synchronized String getName(double x, double y) {
        Location2D pointerLocation = new Location2D(x, y, 1, 1);

        for (ProtoGizmo p : gizmos.values())
            if (p.getPhysicsLocation().contains(pointerLocation))
                return p.getName();


        return "";
    }

    /**
     * This is a method for the model's use - it gets the exact memory references of our gizmos to allow external changes.
     * Utilized by instructions when they are processing/executing.
     * Justification is...optimizations! Don't want to perform several searches for one gizmo.
     *
     * @return The actual gizmos held by this  gizmo model.
     */
    public synchronized HashMap<String, ProtoGizmo> getGizmos() {
        return gizmos;
    }

    /**
     * Notify's all observers that this model has changed.
     */
    private void updateEveryone() {
        super.setChanged();
        super.notifyObservers();
    }

    /**
     * Begins ticking this game's state
     */
    @Override
    public synchronized void run() {
        running = true;
    }

    /**
     * Halts ticking of this game's state
     */
    @Override
    public synchronized void stop() {
        running = false;
    }


    /**
     * Returns a list of names of all gizmos
     */
    @Override
    public List<String> getNames() {
        LinkedList<String> names = new LinkedList<>(gizmos.keySet());
        names.add(walls.getName());
        return names;
    }

    @Override
    public String[] getNonDraggableGizmoTypes() {
        return new String[]{
                GizmoType.Square.toString(),
                GizmoType.Circle.toString(),
                GizmoType.Triangle.toString(),
                GizmoType.LeftFlipper.toString(),
                GizmoType.RightFlipper.toString(),
        };
    }


    @Override
    public String[] getDraggableGizmoTypes() {
        return new String[]{
                GizmoType.Absorber.toString()
        };
    }


    /**
     * @return True iff the game's state is ticking forward. False otherwise.
     */
    @Override
    public synchronized boolean isRunning() {
        return running;
    }

    /**
     * For use by the model - specifically, by Instructions to make changes to this gizmo model.
     *
     * @return The exact memory reference of this gizmo model's constraints.
     */
    public synchronized GameConstraints getConstraints() {
        return constraints;
    }


    public synchronized void addKeyStroke(boolean isUp, Integer c, ProtoGizmo gizmo) {
        if (isUp) {
            if (!keyUp.containsKey(c)) keyUp.put(c, new HashSet<>());
            keyUp.get(c).add(gizmo);
        } else {
            if (!keyDown.containsKey(c)) keyDown.put(c, new HashSet<>());
            keyDown.get(c).add(gizmo);
        }
    }

    public synchronized void removeGizmo(String name) {
        if (name == null)
            return;

        ProtoGizmo gizmoToRemove = getGizmo(name);
        gizmos.remove(name);


        for (ProtoGizmo pair : gizmos.values()) pair.removeConnection(pair);

        removeFromSet(keyDown.values(), gizmoToRemove);
        removeFromSet(keyUp.values(), gizmoToRemove);


    }

    private void removeFromSet(Collection<HashSet<ProtoGizmo>> keys, ProtoGizmo p) {
        for (HashSet<ProtoGizmo> s : keys) s.remove(p);
    }

    public ProtoGizmo getGizmo(String name) {
        return (name == null) ? null : (name.equals("Outer Walls") ? walls : gizmos.get(name));
    }

    @Override
    public List<String> getFreeBallNames() {
        LinkedList<String> strings = new LinkedList<>();
        for (String gizmo : gizmos.keySet()) {
            ProtoGizmo g = gizmos.get(gizmo);
            if (g.getType() == GizmoType.Ball && !g.isFrozen())
                strings.add(gizmo);
        }

        return strings;
    }

    @Override
    public boolean equals(Object o) {
        // Easy stuff first
        // general check
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcreteGizmoModel that = (ConcreteGizmoModel) o;
        // constraits
        if (!constraints.equals(that.constraints)) return false;
        // walls
        if (!walls.equals(that.walls)) return false;

        // Now the tricky bit
        // gizmos hashmap
        // strings
        Collection<String> thisKeys = new HashSet<>(gizmos.keySet());
        Collection<String> thatKeys = new HashSet<>(that.gizmos.keySet());
        if (thisKeys.size() != thatKeys.size()) {
            return false;
        }
        if (!thisKeys.containsAll(thatKeys)) {
            return false;
        }
        // gizmos
        Collection<ProtoGizmo> thisGizmos = gizmos.values();
        Collection<ProtoGizmo> thatGizmos = that.getGizmos().values();
        if (thisGizmos.size() != thatGizmos.size()) {
            return false;
        }
        boolean containsAll = true;

        for (ProtoGizmo p : thatGizmos) {
            containsAll = false;
            for (ProtoGizmo g : thisGizmos) {
                if (p.getName().equals(g.getName()))
                    containsAll = true;
                    //System.out.println(g.getName() + " " + p.getName());
            }
            if (!containsAll) {
                //System.out.println(p);
                return false;
            }
        }

        // KeyUp hashmap
        Collection<Integer> thisKeyUpInts = keyUp.keySet();
        Collection<Integer> thatKeyUpInts = that.keyUp.keySet();
        if (thisKeyUpInts.size() != thatKeyUpInts.size()) {
            return false;
        }
        if (!thisKeyUpInts.containsAll(thatKeyUpInts)) {
            return false;
        }


        if (keyUp.size() != that.keyUp.size())
            return false;

        for (Integer i : keyUp.keySet()) {
            Collection<ProtoGizmo> thisHashSet = keyUp.get(i);
            Collection<ProtoGizmo> thatHashSet = that.keyUp.get(i);
            if (thisHashSet.size() != thatHashSet.size()) {
                return false;
            }
            for (ProtoGizmo g : thisHashSet) {
                boolean cont = false;

                for (ProtoGizmo p : thatHashSet) {
                    if (p.getName().equals(g.getName()))
                        cont = true;
                }

                if (!cont)
                    return false;
            }
        }

        if (keyDown.size() != that.keyDown.size())
            return false;

        for (Integer i : keyDown.keySet()) {
            if (keyDown.get(i) == null)
                if (that.keyDown.get(i) != null)
                    return false;
            if (keyDown.get(i) != null)
                if (that.keyDown.get(i) == null)
                    return false;
            Collection<ProtoGizmo> thisHashSet = keyDown.get(i);
            Collection<ProtoGizmo> thatHashSet = that.keyDown.get(i);
            if (thisHashSet.size() != thatHashSet.size()) {
                return false;
            }

            for (ProtoGizmo g : thisHashSet) {
                boolean cont = false;

                for (ProtoGizmo p : thatHashSet) {
                    if (p.getName().equals(g.getName()))
                        cont = true;
                }

                if (!cont)
                    return false;
            }
        }
        return true;
    }
}
