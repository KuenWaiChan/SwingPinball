package Model;

import Controller.DrawController.iDrawer;
import Model.util.GizmoType;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observer;
import java.util.Set;

public interface IGizmoModel {
    void addObserver(Observer o);

    GizmoType getType(double x, double y);

    void draw(iDrawer canvasPainter);
    void executeInstruction(Instruction i);
    void loadFile(String file) throws FileNotFoundException;
    void writeFile(String file);
    IGizmoModel copy();
    String getName(double x, double y);

    void tick(Set<Integer> keyDown, Set<Integer> keyUp);

    GameConstraints getConstraints();

    boolean isRunning();
    void run();
    void stop();
    List<String> getNames();

	String[] getNonDraggableGizmoTypes();

    String[] getDraggableGizmoTypes();

    List<String> getFreeBallNames();
}
