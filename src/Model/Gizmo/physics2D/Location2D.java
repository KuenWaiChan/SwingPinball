package Model.Gizmo.physics2D;

import Model.Gizmo.GizmoProperties.IBoardPlacement;
import physics.Vect;

import java.awt.geom.Rectangle2D;

public class Location2D implements IBoardPlacement {
    protected double x, y, h, w;
    protected int rot;

    public Location2D(double posx, double posy, double w, double h) {
        if (posx < 0 || posy < 0 || w < 0 || h < 0 || (posx + w) > 20 || (posy + h) > 20)
            throw new IndexOutOfBoundsException("Location is not on the board");
        this.w = w;
        this.h = h;
        this.x = posx;
        this.y = posy;
        this.rot = 0;
    }


    @Override
    public double x() {
        return x;
    }

    @Override
    public double w() {
        return w;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double h() {
        return h;
    }

    /**
     * Rotate this location around the center by a given degrees - this adds to the current rotation. So,
     * if location's current rotation is 20 and  rotateBy(30) is called, rotation is now 50.
     *
     * @param degrees The amount to rotate by
     */
    @Override
    public void rotateBy(int degrees) {
        rot = ((degrees = (degrees + rot) % 360) < 360) ? degrees : 0;
    }

    @Override
    public int r() {
        return rot;
    }

    @Override
    public IBoardPlacement copy() {
        Location2D l = new Location2D(x(), y(), w(), h());
        l.rotateBy(rot);
        return l;
    }

    @Override
    public String toString() {
        return String.format("{'x':'%f', 'y':'%f', 'w':'%f', 'h':'%f', 'rot':'%d'}", x, y, w, h, rot);
    }

    @Override
    public boolean contains(IBoardPlacement l) {
        return new Rectangle2D.Double(x, y, w, h).intersects(l.x(), l.y(), l.w(), l.h());
    }


    @Override
    public void shift(double x, double y) {
        x = this.x + x;
        y = this.y + y;


        //System.out.println("X: " + x + " Y: " + y);
        if (x < 0 ||  y < 0 ||  x+w >=20 ||  y+h >= 20) {
            if(Double.isInfinite(x)){
               this. x=(x<0)?0: (x+h>=20)?19-w:x;
            } else if (Double.isInfinite(y))
            {
                this.y=(y<0)?0:(y+h>=20)?19-h:y;
            }
            throw new IndexOutOfBoundsException("Bad location");
        } else {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public Vect getCenter() {
        return new Vect(this.x + (this.w / 2.0), this.y + (this.h / 2.0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location2D that = (Location2D) o;

        if (Double.compare(that.x, x) != 0) return false;
        if (Double.compare(that.y, y) != 0) return false;
        if (Double.compare(that.h, h) != 0) return false;
        if (Double.compare(that.w, w) != 0) return false;
        return rot == that.rot;
    }
}
