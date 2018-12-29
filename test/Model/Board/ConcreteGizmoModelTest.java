package Model.Board;

import Model.GameConstraints;
import Model.Gizmo.GizmoProperties.IDrawingShape;
import Model.Gizmo.drawing2D.CircleDrawer;
import Model.Gizmo.drawing2D.RectangleDrawer;
import Model.Gizmo.physics2D.Location2D;
import Model.IGizmoModel;
import Model.Instruction;
import Model.ProtoGizmo;
import Model.util.DefaultInstructions;
import Model.util.GizmoType;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import physics.Circle;
import physics.Vect;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

public class ConcreteGizmoModelTest {

    ConcreteGizmoModel emptyModel;
    ConcreteGizmoModel oneBallModel;
    ConcreteGizmoModel oneSquareModel;
    ConcreteGizmoModel ballAndSquareModel;
    ConcreteGizmoModel twoBallsModel;

    String filename1 = "test/testOutput1.txt";

    

    @Before
    public void setUp() {
        emptyModel = new ConcreteGizmoModel(new GameConstraints(), new HashMap<>(), new HashMap<>(), new HashMap<>());

        // Gizmos init
        IDrawingShape ball1Shape = new CircleDrawer(0.25, 0, 0);
        Vect zeroVector = new Vect(0, 0);
        ProtoGizmo ball1 = new ProtoGizmo("ball1", GizmoType.Ball, ball1Shape);
        ball1.setMovable(true);

        Location2D squareLocation = new Location2D(0, 2, 1, 1);
        IDrawingShape squareShape = new RectangleDrawer(1, 1, 0, 1, 0);
        ProtoGizmo square = new ProtoGizmo("square1", GizmoType.Square, squareShape);

        HashMap<String, ProtoGizmo> gizmos = new HashMap<>();

        gizmos.put(ball1.getName() ,ball1);
        oneBallModel = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), new HashMap<>());

        gizmos = new HashMap<>();
        gizmos.put(square.getName(), square);
        oneSquareModel = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), new HashMap<>());

        gizmos = new HashMap<>();
        gizmos.put(ball1.getName(), ball1);
        gizmos.put(square.getName(), square);
        ballAndSquareModel = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), new HashMap<>());

        IDrawingShape collisionBall1Shape = new CircleDrawer(0.25, 0, 0);
        ProtoGizmo collisionBall1 = new ProtoGizmo("collisionBall1", GizmoType.Ball, collisionBall1Shape);
        collisionBall1.setVelocity(new Vect(1, 0));
        collisionBall1.setMovable(true);

        IDrawingShape collisionBall2Shape = new CircleDrawer(0.25, 2.5, 0);
        ProtoGizmo collisionBall2 = new ProtoGizmo("collisionBall2", GizmoType.Ball, collisionBall2Shape);
        collisionBall2.setVelocity(new Vect(-1, 0));
        collisionBall2.setMovable(true);

        gizmos = new HashMap<>();
        gizmos.put(collisionBall1.getName(), collisionBall1);
        gizmos.put(collisionBall2.getName(), collisionBall2);

        GameConstraints gc = new GameConstraints();
        gc.setGravity(0);
        gc.setFriction(0, 0);
        twoBallsModel = new ConcreteGizmoModel(gc, gizmos, new HashMap<>(), new HashMap<>());
    }

     

    @Test
    public void testLoadAndSaveAreEqualGizE1() {
        try {
            emptyModel = new ConcreteGizmoModel();
            ConcreteGizmoModel emptyCopy = (ConcreteGizmoModel) emptyModel.copy();
            emptyModel.loadFile("test/test-files/gizE1.txt");
            emptyModel.writeFile("test/test-files/gizE1Copy.txt");
            emptyCopy.loadFile("test/test-files/gizE1Copy.txt");
            Assert.assertEquals(emptyModel, emptyCopy);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    

    @Test
    public void emptyGizmosSizeTest() {
        assertEquals(0, emptyModel.getGizmos().size());
    }

    @Test
    public void addValidGizmoTest() {
        Instruction addGizmoInst = new DefaultInstructions.AddGizmo(GizmoType.Square, "square", new Location2D(0, 0, 0, 0), Vect.ZERO);
        emptyModel.executeInstruction(addGizmoInst);
        assertEquals(1, emptyModel.getGizmos().size());
    }

    @Test
    public void addSameGizmoTwiceTest() {
        Instruction addGizmoInst1 = new DefaultInstructions.AddGizmo(GizmoType.Square, "square", new Location2D(0, 0, 0, 0), Vect.ZERO);
        emptyModel.executeInstruction(addGizmoInst1);
        emptyModel.executeInstruction(addGizmoInst1);
        assertEquals(1, emptyModel.getGizmos().size());
    }

    @Test
    public void validBoardLoadTest() {
        try {
            emptyModel.loadFile("test/test-files/validTest.txt");
        } catch (FileNotFoundException e) {
            fail("File not found: tes   t/test-files/validTest.txt");
        }
        assertEquals(1, emptyModel.getGizmos().size());
    }

    @Test
    public void overlappingBoardLoadTest() {
        try {
            emptyModel.loadFile("test/test-files/overlapTest.txt");
        } catch (FileNotFoundException e) {
            fail("File not found: test/test-files/overlapTest.txt");
        }

        assertEquals(1, emptyModel.getGizmos().size());
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFileLoadTest() throws FileNotFoundException {
        emptyModel.loadFile("iDontExist.sad.file");
    }

    @Test
    public void writeBoardToFileDoesExistTest() {
        Instruction addGizmoInst1 = new DefaultInstructions.AddGizmo(GizmoType.Square, "square", new Location2D(0, 0, 0, 0), Vect.ZERO);
        emptyModel.executeInstruction(addGizmoInst1);
        emptyModel.writeFile(filename1);
        File f = new File(filename1);
        assertTrue(f.exists());
    }

    @Test
    public void writeBoardToFileAndLoadTest() {
        Instruction addGizmoInst1 = new DefaultInstructions.AddGizmo(GizmoType.Square, "square", new Location2D(0, 0, 0, 0), Vect.ZERO);
        emptyModel.executeInstruction(addGizmoInst1);
        emptyModel.writeFile(filename1);

        ConcreteGizmoModel model2 = new ConcreteGizmoModel();
        try {
            model2.loadFile(filename1);
        } catch (FileNotFoundException e) {
            fail("File not found: " + filename1);
        }

        assertEquals(emptyModel.getGizmos().size(), model2.getGizmos().size());
    }

    // Test whether non-movable square stays in the same place with a tick
    @Test
    public void singleSquareTickTest() {
        Location2D initialLocation = (Location2D) oneSquareModel.getGizmos().values().iterator().next() .getPhysicsLocation();
        double x0 = initialLocation.x();
        double y0 = initialLocation.y();
        oneSquareModel.tick(null, null);
        oneSquareModel.tick(null, null);
        Location2D newLocation = (Location2D) oneSquareModel.getGizmos().values().iterator().next() .getPhysicsLocation();
        assertEquals(x0, newLocation.x(), 0);
        assertEquals(y0, newLocation.y(), 0);
    }

    // Test whether ball moves down with a tick
    @Test
    public void ballTickTest() {
        Location2D initialLocation = (Location2D) oneBallModel.getGizmos().values().iterator().next().getPhysicsLocation();
        double x0 = initialLocation.x();
        double y0 = initialLocation.y();
        oneBallModel.tick(null, null);
        oneBallModel.tick(null, null); //Requires two ticks since gravity/friction is added at the end of the tick.
        Location2D newLocation = (Location2D) oneBallModel.getGizmos().values().iterator().next().getPhysicsLocation();
        assertEquals(x0, newLocation.x(), 0);
        assertNotEquals(y0, newLocation.y());
        assertTrue(y0 < newLocation.y());
    }

    // Test whether ball hits back from the square
    @Test
    public void ballBounceTest() throws Exception {
        double maxYVal = 0;
        // Tick n times and record max .y() value of the ball
        int numberOfTicks = 50;
        for (int i = 0; i < numberOfTicks; i++) {
            ProtoGizmo ball = ballAndSquareModel.getGizmo("ball1");
            if (ball.getPhysicsLocation().y() > maxYVal)
                maxYVal = ball.getPhysicsLocation().y();
            ballAndSquareModel.tick(null, null);
        }

        // Get square Y value
        double squareYValue = ballAndSquareModel.getGizmo("square1").getPhysicsLocation().y();

        assertTrue(maxYVal <= squareYValue);
    }

    @Test
    public void ballsCollisionTest() throws Exception {
        // save initial locations
        double initialXBall1 = 0;
        double initialXBall2 = 0;
        Vect vectorBall1 = null;
        Vect vectorBall2 = null;

        for (ProtoGizmo gizmo : twoBallsModel.getGizmos().values()) {
            if (gizmo.getName().equals("collisionBall1")) {
                vectorBall1 = gizmo.getVelocity();
                initialXBall1 = gizmo.getPhysicsLocation().x();
            } else {
                vectorBall2 = gizmo.getVelocity();
                initialXBall2 = gizmo.getPhysicsLocation().x();
            }
        }
        // tick for the time that takes to move by the gizmo vector
        for (int i = 0; i < new GameConstraints().TICKS; i++) {
            twoBallsModel.tick(null, null);
        }
        // balls should be exactly next to each other now with x = initialX + vector.x
        double currentXBall1 = 0;
        double currentXBall2 = 0;
        for (ProtoGizmo gizmo : twoBallsModel.getGizmos().values()) {
            if (gizmo.getName().equals("collisionBall1")) {
                currentXBall1 = gizmo.getPhysicsLocation().x();
            } else {
                currentXBall2 = gizmo.getPhysicsLocation().x();
            }
        }
        double floatingPointDelta = 0.00000005;
        assertEquals(initialXBall1 + vectorBall1.x(), currentXBall1, floatingPointDelta);
        assertEquals(initialXBall2 + vectorBall2.x(), currentXBall2, floatingPointDelta);
        // final collision tick
        twoBallsModel.tick(null, null);
        // get final positions
        double finalXBall1 = 0;
        double finalXBall2 = 0;
        for (ProtoGizmo gizmo : twoBallsModel.getGizmos().values()) {
            if (gizmo.getName().equals("collisionBall1")) {
                finalXBall1 = gizmo.getPhysicsLocation().x();
            } else {
                finalXBall2 = gizmo.getPhysicsLocation().x();
            }
        }
        assertTrue(finalXBall1 < currentXBall1);
        assertTrue(finalXBall2 > currentXBall2);
    }

    @Test
    public void copyModelTest() throws Exception {
        // add random keymappings
        int keyCode = 65;
        boolean up = false;
        for(ProtoGizmo gizmo : twoBallsModel.getGizmos().values()) {
            twoBallsModel.addKeyStroke(up, keyCode, gizmo);
            up = !up;
            keyCode++;
        }

        ConcreteGizmoModel copiedModel = (ConcreteGizmoModel) twoBallsModel.copy();

        Assert.assertEquals(twoBallsModel, copiedModel);
    }


    @Test
    public void getValidNameTest() throws Exception {
        ProtoGizmo square = oneSquareModel.getGizmos().values().iterator().next();
        Location2D location = (Location2D) square.getPhysicsLocation();
        Assert.assertEquals(square.getName(), oneSquareModel.getName(location.x(), location.y()));
    }

    @Test
    public void getInvalidNameTest() throws Exception {
        Assert.assertEquals("", oneSquareModel.getName(5, 5));
    }

    @Test
    public void triggerKeys1Key2Gizmo() {
        Set<Integer> s = new HashSet<Integer>();
        s.add(1);
        oneSquareModel.addKeyStroke(false, 1, oneSquareModel.getGizmo("square1"));
        oneSquareModel.executeInstruction(new DefaultInstructions.AddGizmo(GizmoType.RightFlipper, "flip", new Location2D(0, 5, 2, 2), new Vect(0, 0)));
        oneSquareModel.executeInstruction(new DefaultInstructions.Connect("square1", "flip"));
        oneSquareModel.tick(s, null);
        ProtoGizmo g = oneSquareModel.getGizmo("square1");
        Assert.assertEquals(g.getColor(), g.getAlternateColor());
    }

    @Test
    public void triggerKeys2Keys2Gizmo() {
        Set<Integer> sd = new HashSet<Integer>();
        Set<Integer> su = new HashSet<Integer>();
        sd.add(1);
        su.add(2);
        oneSquareModel.addKeyStroke(false, 1, oneSquareModel.getGizmo("square1"));
        oneSquareModel.addKeyStroke(true, 2, oneSquareModel.getGizmo("square1"));
        oneSquareModel.executeInstruction(new DefaultInstructions.Connect("square1", "square1"));
        oneSquareModel.tick(sd, su);
        ProtoGizmo g = oneSquareModel.getGizmo("square1");
        Assert.assertEquals(g.getColor(), g.getAlternateColor());
    }

    @Test
    public void testInitialRunning() throws Exception {
        Assert.assertEquals(false, oneSquareModel.isRunning());
    }

    @Test
    public void testRunAfterInitial() throws Exception {
        oneSquareModel.run();
        Assert.assertEquals(true, oneSquareModel.isRunning());
    }

    @Test
    public void testStopAfterInitial() throws Exception {
        oneSquareModel.stop();
        Assert.assertEquals(false, oneSquareModel.isRunning());
    }

    @Test
    public void testStopWhenRunning() throws Exception {
        oneSquareModel.run();
        oneSquareModel.stop();
        Assert.assertEquals(false, oneSquareModel.isRunning());
    }

    @Test
    public void testGetNames() throws Exception {
        List<String> expectedNames = new ArrayList<>();
        ProtoGizmo square = oneSquareModel.getGizmos().values().iterator().next();
        expectedNames.add(square.getName());
        expectedNames.add("OuterWalls");
        Assert.assertEquals(expectedNames, oneSquareModel.getNames());
    }

    @Test
    public void removeValidGizmoTest() throws Exception {
        int initialSize = oneSquareModel.getGizmos().size();
        ProtoGizmo square = oneSquareModel.getGizmos().values().iterator().next();
        oneSquareModel.removeGizmo(square.getName());
        Assert.assertEquals(initialSize - 1, oneSquareModel.getGizmos().size());
    }

    @Test
    public void removeNullGizmoTest() throws Exception {
        int initialSize = oneSquareModel.getGizmos().size();
        oneSquareModel.removeGizmo(null);
        Assert.assertEquals(initialSize, oneSquareModel.getGizmos().size());
    }

    @Test
    public void removeInvalidGizmoTest() throws Exception {
        int initialSize = oneSquareModel.getGizmos().size();
        oneSquareModel.removeGizmo("Something stupid");
        Assert.assertEquals(initialSize, oneSquareModel.getGizmos().size());
    }

    @Test
    public void unequalGizmoNamesSameTypesTest() throws Exception {
        ConcreteGizmoModel model1;
        ConcreteGizmoModel model2;
        HashMap<String, ProtoGizmo> gizmos1 = new HashMap<>();
        HashMap<String, ProtoGizmo> gizmos2 = new HashMap<>();

        IDrawingShape squareShape = new RectangleDrawer(1, 1, 0, 1, 0);
        ProtoGizmo square = new ProtoGizmo("square1", GizmoType.Square, squareShape);

        gizmos1.put("square", square);
        gizmos2.put("skuare", square); // notice name difference

        model1 = new ConcreteGizmoModel(new GameConstraints(), gizmos1, new HashMap<>(), new HashMap<>());
        model2 = new ConcreteGizmoModel(new GameConstraints(), gizmos2, new HashMap<>(), new HashMap<>());

        Assert.assertNotEquals(model1, model2);
    }

    @Test
    public void unequalGizmoDiffentKeyUpMappings() throws Exception {
        ConcreteGizmoModel model1;
        ConcreteGizmoModel model2;
        HashMap<String, ProtoGizmo> gizmos = new HashMap<>();

        IDrawingShape squareShape = new RectangleDrawer(1, 1, 0, 1, 0);
        ProtoGizmo square = new ProtoGizmo("square1", GizmoType.Square, squareShape);
        gizmos.put("square", square);

        HashSet<ProtoGizmo> gizmoSet = new HashSet<>();
        gizmoSet.add(square);

        HashMap<Integer, HashSet<ProtoGizmo>> keyUpMapping1 = new HashMap<>();
        HashMap<Integer, HashSet<ProtoGizmo>> keyUpMapping2 = new HashMap<>();

        keyUpMapping1.put(37, gizmoSet);
        keyUpMapping2.put(38, gizmoSet); // notice different key value

        model1 = new ConcreteGizmoModel(new GameConstraints(), gizmos, keyUpMapping1, new HashMap<>());
        model2 = new ConcreteGizmoModel(new GameConstraints(), gizmos, keyUpMapping2, new HashMap<>());

        Assert.assertNotEquals(model1, model2);
    }

    @Test
    public void unequalGizmoDiffentKeyUpMappingGizmoSetSize() throws Exception {
        ConcreteGizmoModel model1;
        ConcreteGizmoModel model2;
        HashMap<String, ProtoGizmo> gizmos = new HashMap<>();

        IDrawingShape squareShape1 = new RectangleDrawer(1, 1, 0, 1, 0);
        IDrawingShape squareShape2 = new RectangleDrawer(1, 1, 1, 2, 0);
        ProtoGizmo square1 = new ProtoGizmo("square1", GizmoType.Square, squareShape1);
        ProtoGizmo square2 = new ProtoGizmo("square2", GizmoType.Square, squareShape2);
        gizmos.put("square1", square1);
        gizmos.put("square2", square2);

        HashSet<ProtoGizmo> gizmoSet1 = new HashSet<>();
        gizmoSet1.add(square1);
        gizmoSet1.add(square2);

        HashSet<ProtoGizmo> gizmoSet2 = new HashSet<>();
        gizmoSet2.add(square1);

        HashMap<Integer, HashSet<ProtoGizmo>> keyUpMapping1 = new HashMap<>();
        HashMap<Integer, HashSet<ProtoGizmo>> keyUpMapping2 = new HashMap<>();

        keyUpMapping1.put(37, gizmoSet1);
        keyUpMapping2.put(37, gizmoSet2); // notice different sets

        model1 = new ConcreteGizmoModel(new GameConstraints(), gizmos, keyUpMapping1, new HashMap<>());
        model2 = new ConcreteGizmoModel(new GameConstraints(), gizmos, keyUpMapping2, new HashMap<>());

        Assert.assertNotEquals(model1, model2);
    }

    // These names... sorry
    @Test
    public void unequalGizmoDiffentKeyUpMappingGizmoSetGizmos() throws Exception {
        ConcreteGizmoModel model1;
        ConcreteGizmoModel model2;
        HashMap<String, ProtoGizmo> gizmos = new HashMap<>();

        IDrawingShape squareShape1 = new RectangleDrawer(1, 1, 0, 1, 0);
        IDrawingShape squareShape2 = new RectangleDrawer(1, 1, 1, 2, 0);
        ProtoGizmo square1 = new ProtoGizmo("square1", GizmoType.Square, squareShape1);
        ProtoGizmo square2 = new ProtoGizmo("square2", GizmoType.Square, squareShape2);
        gizmos.put("square1", square1);
        gizmos.put("square2", square2);

        HashSet<ProtoGizmo> gizmoSet1 = new HashSet<>();
        gizmoSet1.add(square1);


        HashSet<ProtoGizmo> gizmoSet2 = new HashSet<>();
        gizmoSet2.add(square2); // notice different gizmos

        HashMap<Integer, HashSet<ProtoGizmo>> keyUpMapping1 = new HashMap<>();
        HashMap<Integer, HashSet<ProtoGizmo>> keyUpMapping2 = new HashMap<>();

        keyUpMapping1.put(37, gizmoSet1);
        keyUpMapping2.put(37, gizmoSet2);

        model1 = new ConcreteGizmoModel(new GameConstraints(), gizmos, keyUpMapping1, new HashMap<>());
        model2 = new ConcreteGizmoModel(new GameConstraints(), gizmos, keyUpMapping2, new HashMap<>());

        Assert.assertNotEquals(model1, model2);
    }

    @Test
    public void unequalGizmoDiffentKeyDownMappings() throws Exception {
        ConcreteGizmoModel model1;
        ConcreteGizmoModel model2;
        HashMap<String, ProtoGizmo> gizmos = new HashMap<>();

        IDrawingShape squareShape = new RectangleDrawer(1, 1, 0, 1, 0);
        ProtoGizmo square = new ProtoGizmo("square1", GizmoType.Square, squareShape);
        gizmos.put("square", square);

        HashSet<ProtoGizmo> gizmoSet = new HashSet<>();
        gizmoSet.add(square);

        HashMap<Integer, HashSet<ProtoGizmo>> keyDownMapping1 = new HashMap<>();
        HashMap<Integer, HashSet<ProtoGizmo>> keyDownMapping2 = new HashMap<>();

        keyDownMapping1.put(37, gizmoSet);
        keyDownMapping2.put(38, gizmoSet); // notice different key value

        model1 = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), keyDownMapping1);
        model2 = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), keyDownMapping2);

        Assert.assertNotEquals(model1, model2);
    }

    @Test
    public void unequalGizmoDiffentKeyDownMappingGizmoSetSize() throws Exception {
        ConcreteGizmoModel model1;
        ConcreteGizmoModel model2;
        HashMap<String, ProtoGizmo> gizmos = new HashMap<>();

        IDrawingShape squareShape1 = new RectangleDrawer(1, 1, 0, 1, 0);
        IDrawingShape squareShape2 = new RectangleDrawer(1, 1, 1, 2, 0);
        ProtoGizmo square1 = new ProtoGizmo("square1", GizmoType.Square, squareShape1);
        ProtoGizmo square2 = new ProtoGizmo("square2", GizmoType.Square, squareShape2);
        gizmos.put("square1", square1);
        gizmos.put("square2", square2);

        HashSet<ProtoGizmo> gizmoSet1 = new HashSet<>();
        gizmoSet1.add(square1);
        gizmoSet1.add(square2);

        HashSet<ProtoGizmo> gizmoSet2 = new HashSet<>();
        gizmoSet2.add(square1);

        HashMap<Integer, HashSet<ProtoGizmo>> keyDownMapping1 = new HashMap<>();
        HashMap<Integer, HashSet<ProtoGizmo>> keyDownMapping2 = new HashMap<>();

        keyDownMapping1.put(37, gizmoSet1);
        keyDownMapping2.put(37, gizmoSet2); // notice different sets

        model1 = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), keyDownMapping1);
        model2 = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), keyDownMapping2);

        Assert.assertNotEquals(model1, model2);
    }


    @Test
    public void getNonDraggableGizmoTypesTest() {
        String[] nondrag = {
            GizmoType.Square.toString(),
                    GizmoType.Circle.toString(),
                    GizmoType.Triangle.toString(),
                    GizmoType.LeftFlipper.toString(),
                    GizmoType.RightFlipper.toString(),
        };

        Assert.assertTrue(Arrays.equals(nondrag,oneSquareModel.getNonDraggableGizmoTypes()));
    }

    @Test
    public void getDraggableGizmoTypesTest(){
        String[] drag = {GizmoType.Absorber.toString()};

        Assert.assertTrue(Arrays.equals(drag,oneSquareModel.getDraggableGizmoTypes()));
    }

    @Test
    public void getFreeBallNamesTest() throws Exception {
        ConcreteGizmoModel model;
        HashMap<String, ProtoGizmo> gizmos = new HashMap<>();

        CircleDrawer cd1 = new CircleDrawer(0.25, 1, 1);
        ProtoGizmo ball = new ProtoGizmo("ball", GizmoType.Ball, cd1);

        CircleDrawer cd2 = new CircleDrawer(0.25, 2, 3);
        ProtoGizmo frozenBall = new ProtoGizmo("frozenBall", GizmoType.Ball, cd2);
        frozenBall.setFrozen(true);

        gizmos.put(ball.getName(), ball);
        gizmos.put(frozenBall.getName(), frozenBall);

        model = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), new HashMap<>());
        Assert.assertEquals(1, model.getFreeBallNames().size());
    }

    @Test
    public void copyModelWithConnectionTest() throws Exception {
        HashMap<String, ProtoGizmo> gizmos = new HashMap<>();

        RectangleDrawer drawer1 = new RectangleDrawer(1, 1, 1, 1, 0);
        RectangleDrawer drawer2 = new RectangleDrawer(1, 1, 2, 1, 0);
        ProtoGizmo square1 = new ProtoGizmo("square1", GizmoType.Square, drawer1);
        ProtoGizmo square2 = new ProtoGizmo("square2", GizmoType.Square, drawer2);

        square1.addGizmo(square2);

        gizmos.put(square1.getName(), square1);
        gizmos.put(square2.getName(), square2);

        ConcreteGizmoModel model = new ConcreteGizmoModel(new GameConstraints(), gizmos, new HashMap<>(), new HashMap<>());
        ConcreteGizmoModel copyModel = (ConcreteGizmoModel) model.copy();

        Assert.assertEquals(model, copyModel);
    }

     

    @After
    public void cleanUp() {
        File f = new File(filename1);
        f.delete();
        f = new File("test/test-files/gizE1Copy.txt");
        f.delete();
    }
}
