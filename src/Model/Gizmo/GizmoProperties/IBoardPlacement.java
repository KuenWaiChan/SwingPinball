package Model.Gizmo.GizmoProperties;

import physics.Vect;

import java.awt.*;

public interface IBoardPlacement {
    double x();

    double w();

    double y();

    double h();


    void rotateBy(int degrees);

    int r();

    IBoardPlacement copy();

    boolean contains(IBoardPlacement l);

    void shift(double x, double y);

    Vect getCenter();

}
