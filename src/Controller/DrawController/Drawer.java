package Controller.DrawController;

import Model.IGizmoModel;
import javafx.util.Pair;
import physics.LineSegment;
import physics.Vect;

import javax.sound.sampled.Line;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public  class Drawer implements iDrawer, iDrawerView, Observer {
    private   List<Pair<Color, Shape>> rects;
    private   List<Pair<Color, Shape>> lines;
    private   List<Pair<Color, Shape>> polygons;
    private   List<Pair<Color, Shape>> circles;
    private   List<Pair<Color, String>> alerts;
    private  List<Pair<Color, Shape>> roundedRects;
    private   IGizmoModel model;

    /**
     * Takes a IGizmoModel and initialises the Drawer and adds this as observer
     *
     * @param model != null
     */
    public  Drawer(IGizmoModel model) {
        this.model = model;
        rects = new ArrayList<>();
        lines = new ArrayList<>();
        circles = new ArrayList<>();
        alerts = new ArrayList<>();
        polygons = new ArrayList<>();
        roundedRects = new ArrayList<>();
        if (model != null) {
            model.addObserver(this);
            model.draw(this);
        }
    }
    @Override
    public synchronized Shape drawLine(Vect p1, Vect p2, Color col) {

        Line2D l = new Line2D.Double(new Point2D.Double(p1.x() ,p1.y() ), new Point2D.Double(p2.x() , p2.y()));
        lines.add(new Pair(col, l));
        return l;
    }

    @Override
    public synchronized Shape[] drawBox(Vect topLeftCorner, Vect topRightCorner, Vect bottomLeftCorner, Vect bottomRightCorner, Color col) {
        Line2D l1 = (Line2D) drawLine(topLeftCorner, topRightCorner, Color.red),
                l2 = (Line2D) drawLine(topLeftCorner, bottomLeftCorner, Color.black),
                l3 = (Line2D) drawLine(topRightCorner, bottomRightCorner, Color.green),
                l4 = (Line2D) drawLine(bottomLeftCorner, bottomRightCorner, Color.blue);
        //        Line2D trace = (Line2D) drawLine(topLeftCorner,bottomRightCorner, Color.black);
        Line2D[] ls = {l1, l2, l3, l4};
        return ls;
    }



    @Override
    public synchronized Shape drawRectangle(double x, double y, double l, double h, Color c) {
        ////////System.out.println.out.println("Drawer: Added Rectangle  nyaa");
        Rectangle2D r = new Rectangle2D.Double(x, y, l, h);
        rects.add(new Pair<>(c, r));
        return r;
    }

    /**
     * Adds a Circle with the dimensions and colour to be drawn
     *
     * @param x
     * @param y
     * @param r
     * @param c != null
     * @return Shape that is created
     */
    @Override
    public synchronized Shape drawCircle(double x, double y, double r, Color c) {
        Shape circ = new Ellipse2D.Double(x, y, r * 2, r * 2);
        circles.add(new Pair(c, circ));
        return circ;
    }

    /**
     * Adds a polygon with the points from the list of x,y coordinated and a color
     *
     * @param points != null
     * @param c      != null
     * @return Shape that is created
     */
    @Override
    public synchronized Shape drawPolygon(List<Pair<Double, Double>> points, Color c) {
        ////("Drawer : Added Polygon nyaa");
        Polygon d = new Polygon();
        for (Pair<Double, Double> pair : points) {
            double x = pair.getKey();
            double y = pair.getValue();
            d.addPoint((int) x, (int) y);
        }
        polygons.add(new Pair<>(c, d));
        return d;
    }


    /**
     * Adds a Rounded rectangle with the dimensions and uses right for if it is flipped or not, with a colour.
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param right
     * @param c     != null
     * @return Shape that is created
     */
    @Override
    public synchronized Shape drawRoundedRectangle(double x, double y, double w, double h, boolean right, Color c) {
        Rectangle2D r = new Rectangle2D.Double(x, y, w, h);
        roundedRects.add(new Pair<>(c, new RoundRect(x, y, x + w, y + h, right)));
        return r;
    }


    /**
     * Adds a rounded rectangle between x1,y1 and x2,y2 and if it is flipped. Is paired with the colour.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param right
     * @param c     != null
     * @return Shape that is created
     */
    @Override
    public synchronized Shape drawPrecisionRoundedRectangle(double x1, double y1, double x2, double y2, boolean right, Color c) {
        Rectangle2D r = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
        roundedRects.add(new Pair<>(c, new RoundRect(x1, y1, x2, y2, right)));
        return r;
    }


    /**
     * Adds a note to alert the user at the next connivance with the string and colour.
     *
     * @param s != null
     * @param c != null
     */
    @Override
    public synchronized void alertUser(String s, Color c) {
        alerts.add(new Pair<>(c, s));
    }


    /**
     * Clears all the drawable items from the drawer;
     */
    @Override
    public synchronized void clear() {
        lines.clear();
        rects.clear();
        circles.clear();
        polygons.clear();
        roundedRects.clear();
        alerts.clear();

    }

    /**
     * Scales all rounded rectangles and then returns them.
     *
     * @param scaleX > 0
     * @param scaleY > 0
     * @return List of all rounded rectangles and their corresponding colour.
     */
    private synchronized List<Pair<Color, RoundRect>> getRoundedRects(double scaleX, double scaleY) {
        ArrayList<Pair<Color, RoundRect>> returnable = new ArrayList<>();
        roundedRects.forEach(recs -> returnable.add(new Pair<>(recs.getKey(), ((RoundRect) recs.getValue()).getScaledRect(scaleX, scaleY))));
        return returnable;
    }

    /**
     * Scales all rectangles and then returns them.
     *
     * @param scaleX > 0
     * @param scaleY > 0
     * @return List of all rectangles and their corresponding colour.
     */
    private synchronized List<Pair<Color, Rectangle2D>> getRectangles(double scaleX, double scaleY) {
        ArrayList<Pair<Color, Rectangle2D>> returnable = new ArrayList<>();
        for (Pair<Color, Shape> recs : rects) {
            Rectangle2D rec = recs.getValue().getBounds2D();
            double x = rec.getX() * scaleX;
            double y = rec.getY() * scaleY;
            double h = rec.getHeight() * scaleY;
            double w = rec.getWidth() * scaleX;
            returnable.add(new Pair<>(recs.getKey(), new Rectangle2D.Double(x, y, w, h)));
        }
        ////("Drawer : Drawable rectangles : " + returnable.size());
        return returnable;
    }

    /**
     * Scales all circles and then returns them.
     *
     * @param scaleX > 0
     * @param scaleY > 0
     * @return List of all circles and their corresponding colour.
     */
    private synchronized List<Pair<Color, Ellipse2D>> getCircles(double scaleX, double scaleY) {
        ArrayList<Pair<Color, Ellipse2D>> returnable = new ArrayList<>();
        for (Pair<Color, Shape> shaper : circles) {
            Ellipse2D circle = (Ellipse2D) shaper.getValue();

            double x = circle.getX() * scaleX;
            double y = circle.getY() * scaleY;
            double h = circle.getHeight() * scaleY;
            double w = circle.getWidth() * scaleX;
            returnable.add(new Pair<>(shaper.getKey(), new Ellipse2D.Double(x, y, w, h)));
        }
        ////("Drawer : Drawable circles : " + returnable.size());
        return returnable;
    }

    /**
     * Scales all Lines and then returns them.
     *
     * @param scaleX > 0
     * @param scaleY > 0
     * @return List of all Lines and their corresponding colour.
     */
    private synchronized List<Pair<Color, Line2D>> getLines(double scaleX, double scaleY) {
        ArrayList<Pair<Color, Line2D>> returnable = new ArrayList<>();
        for (Pair<Color, Shape> shaper : lines) {
            Line2D line = (Line2D) shaper.getValue();

            double x1 = line.getX1() * scaleX;
            double y1 = line.getY1() * scaleY;
            double x2 = line.getX2() * scaleX;
            double y2 = line.getY2() * scaleY;
            returnable.add(new Pair<>(shaper.getKey(), new Line2D.Double(x1, y1, x2, y2)));
        }
        ////("Drawable lines : " + returnable.size());
        return returnable;
    }

    /**
     * Scales all Polygons and then returns them.
     *
     * @param scaleX > 0
     * @param scaleY > 0
     * @return List of all Polygons and their corresponding colour.
     */
    private synchronized List<Pair<Color, Polygon>> getPolygons(double scaleX, double scaleY) {
        List<Pair<Color, Polygon>> poli = new ArrayList<>();
        for (Pair<Color, Shape> shaper : polygons) {
            Polygon pol = (Polygon) shaper.getValue();
            Polygon p = new Polygon();
            for (int i = 0; i < pol.npoints; i++) {
                p.addPoint((int) (pol.xpoints[i] * scaleX), (int) (pol.ypoints[i] * scaleY));
            }
            poli.add(new Pair<>(shaper.getKey(), p));
        }
        return poli;
    }

    /**
     * Gets a fresh list of all alerts that are needed to be sent to the user
     *
     * @return
     */
    @Override
    public  synchronized List<Pair<Color, String>> getMessages() {
        ArrayList<Pair<Color, String>> returnable = new ArrayList<>();
        returnable.addAll(alerts);
        return returnable;
    }

    /**
     * Check if there are messages for the view
     *
     * @return boolean true for has messages, false for not
     */
    @Override
    public  synchronized Boolean hasMessages() {
        return !alerts.isEmpty();
    }

    /**
     * Clears the message list
     */
    @Override
    public   synchronized void clearMessages() {
        alerts.clear();
    }

    /**
     * Draws the shapes that need to be drawn onto the graphics scaled at the ratios of scaleX and scaleY
     *
     * @param realImage != null
     * @param scaleX
     * @param scaleY
     */
    @Override
    public   synchronized void draw(Graphics realImage, double scaleX, double scaleY) {

        BufferedImage buffer = new BufferedImage(20 * (int) scaleX, 20 * (int) scaleY, BufferedImage.TYPE_INT_RGB);
        Graphics g3 = buffer.getGraphics();
        g3.setColor(Color.white);
        g3.fillRect(0, 0, 20 * (int) scaleX, 20 * (int) scaleY);


        Graphics2D g = (Graphics2D) g3;

        for (Pair<Color, Line2D> l : getLines(scaleX, scaleY)) {
            g.setColor(l.getKey());
            final int STROKE_SIZE = (int) scaleX;
            float stroke = STROKE_SIZE * 0.51f;
            Stroke s = g.getStroke();
            g.setStroke(new BasicStroke(stroke, BasicStroke.CAP_BUTT, 1, 0, null, 1));

            g.drawLine((int) l.getValue().getX1(), (int) l.getValue().getY1(), (int) l.getValue().getX2(), (int) l.getValue().getY2());
            g.setStroke(s);
        }

        for (Pair<Color, RoundRect> r : getRoundedRects(scaleX, scaleY)) {

            g.setColor(r.getKey());
            final int STROKE_SIZE = (int) scaleX;
            float stroke = STROKE_SIZE * 0.38f;
            //int x1 = (int) r.getValue().x1 + STROKE_SIZE / 2;

            int x1 = (int) (r.getValue().x1 + (stroke / 2));
            int x2 = (int) (r.getValue().x2 + (stroke / 2));
            if (r.getValue().right) {
                x1 = (int) (r.getValue().x1 - (stroke / 2));
                x2 = (int) (r.getValue().x2 - (stroke / 2));
            }
            int y1 = (int) (r.getValue().y1 + (stroke / 2));
            int y2 = (int) (r.getValue().y2 - (stroke / 2));
            g.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, 1, 0, null, 1));
            g.drawLine(x1, y1, x2, y2);
            g.setStroke(new BasicStroke(0f, BasicStroke.CAP_ROUND, 1, 1, null, 1));
        }
        for (Pair<Color, Rectangle2D> r : getRectangles(scaleX, scaleY)) {

            g.setColor(r.getKey());
            g.fillRect((int) r.getValue().getX(), (int) r.getValue().getY(), (int) r.getValue().getWidth(), (int) r.getValue().getHeight());
        }


        for (Pair<Color, Polygon> p : getPolygons(scaleX, scaleY)) {

            g.setColor(p.getKey());
            g.fillPolygon(p.getValue());
            g.setColor(Color.black);
//            g.drawPolygon(p.getValue());
        }

        synchronized (this){
            for (Pair<Color, Ellipse2D> e : getCircles(scaleX, scaleY)) {
                g.setColor(e.getKey());
                g.fillOval((int) e.getValue().getX(), (int) e.getValue().getY(), (int) e.getValue().getWidth(), (int) e.getValue().getHeight());
            }}

        realImage.drawImage(buffer, 0, 0, null);

    }

    /**
     * Removes the shape from all draw lists
     *
     * @param s != null
     */

    @Override
    public   synchronized void clearShape(Shape s) {

        if (contains(s, polygons)) {
            polygons.remove(s);
            return;
        }
        if (contains(s, this.circles)) {
            circles.remove(s);
            return;
        }
        if (contains(s, this.lines)) {
            lines.remove(s);
            return;
        }
        if (contains(s, this.rects)) {
            rects.remove(s);
            return;
        }
        if (contains(s, this.roundedRects)) {
            roundedRects.remove(s);
            return;
        }

    }

    /**
     * Checks whether s is in the List e or not
     *
     * @param s != null
     * @param e != null
     * @return boolean : true if contained, false if not
     */

    private   synchronized boolean contains(Shape s, List<Pair<Color, Shape>> e) {
        for (Pair<Color, Shape> o : e)
            if (o.getValue().equals(s))
                return true;

        return false;

    }

    /**
     * Draws a grid of 20 * 20 at a scale of scaleX and scaleY onto g
     *
     * @param g      != null, Graphics to be drawn onto
     * @param scaleX
     * @param scaleY
     * @param height
     * @param width
     */
    @Override
    public   synchronized void drawGrid(Graphics g, double scaleX, double scaleY, int height, int width) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.black);
        for (int i = 0; i < 21; i++) {
            g2.draw(new Line2D.Double(scaleX * i, 0, scaleX * i, height));
            g2.draw(new Line2D.Double(0, scaleY * i, width, scaleY * i));
        }
    }


    /**
     * Inherited from observer, on update gets the most recent edition from the model
     *
     * @param o
     * @param arg
     */
    @Override
    public synchronized void update(Observable o, Object arg) {
        ////////System.out.println.out.println("Drawer: update: Updated");
        model.draw(this);
    }

/**
 * Used to store the data needed for the rounded rectangle
 */
//TODO: Make into da shape
private  class RoundRect implements Shape {
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private   boolean right;

    public   RoundRect(double x1, double y1, double x2, double y2, boolean right) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.right = right;
    }

    /**
     * Duplicates this rectangles data and scales it to the ratio of scaleX scaleY
     *
     * @param scaleX
     * @param scaleY
     * @return A scaled RoundRect
     */
    public synchronized RoundRect getScaledRect(double scaleX, double scaleY) {
        return new RoundRect(x1 * scaleX, y1 * scaleY, x2 * scaleX, y2 * scaleY, right);
    }

    @Override
    public synchronized Rectangle getBounds() {
        return new Rectangle((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
    }

    @Override
    public synchronized Rectangle2D getBounds2D() {
        return new Rectangle2D.Double(x1, y1, (x2 - x1), (y2 - y1));
    }

    @Override
    public synchronized boolean contains(double x, double y) {
        return getBounds().contains(x, y);
    }

    @Override
    public synchronized boolean contains(Point2D p) {
        return getBounds().contains(p);
    }

    @Override
    public synchronized boolean intersects(double x, double y, double w, double h) {
        return getBounds().intersects(x, y, w, h);
    }

    @Override
    public synchronized boolean intersects(Rectangle2D r) {
        return getBounds().intersects(r);
    }

    @Override
    public synchronized boolean contains(double x, double y, double w, double h) {
        return getBounds().contains(x, y, w, h);
    }

    @Override
    public synchronized boolean contains(Rectangle2D r) {
        return getBounds().contains(r);
    }

    @Override
    public synchronized PathIterator getPathIterator(AffineTransform at) {
        return getBounds().getPathIterator(at);
    }

    @Override
    public synchronized PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getBounds().getPathIterator(at, flatness);
    }
}
}
