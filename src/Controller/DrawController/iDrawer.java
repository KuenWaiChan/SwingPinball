package Controller.DrawController;

import javafx.util.Pair;
import physics.Vect;

import java.awt.*;
import java.util.List;

public interface iDrawer {
    Shape drawRectangle(double x, double y, double l, double h, Color c);
    Shape drawCircle(double x, double y, double r, Color c);
    void alertUser(String s, Color c);
    void clear();
    Shape drawPolygon(List<Pair<Double, Double>> points, Color c);

    Shape drawRoundedRectangle(double x, double y, double w, double h, boolean right, Color c);
    Shape drawPrecisionRoundedRectangle(double x1, double y1, double x2, double y2, boolean right, Color c);
    void clearShape(Shape s);

    Shape drawLine(Vect topLeftCorner, Vect bottomRightCorner, Color col);

    Shape[] drawBox(Vect topLeftCorner, Vect topRightCorner, Vect bottomLeftCorner, Vect bottomRightCorner, Color col);

}
