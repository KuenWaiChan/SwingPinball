package Controller.DrawController;


import javafx.util.Pair;
import physics.Vect;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class iDrawerTest implements iDrawer{

	public int
			rectangles = 0,
			circle = 0,
			strings = 0,
			polygon = 0,
			roundedrec = 0,
			lines = 0;
	@Override
	public Shape drawRectangle(double x, double y, double l, double h, Color c) {
		rectangles ++;
		return new Rectangle.Double(x,y,l,h);
	}

	@Override
	public Shape drawCircle(double x, double y, double r, Color c) {
		circle ++;
		return new Ellipse2D.Double(x,y,r*2, r*2);
	}

	@Override
	public void alertUser(String s, Color c) {
	strings ++;
	}

	@Override
	public void clear() {
		rectangles = 0;
				circle = 0;
				strings = 0;
				polygon = 0;
				roundedrec = 0;
				lines = 0;
	}

	@Override
	public Shape drawPolygon(List<Pair<Double, Double>> points, Color c) {
		polygon ++;Polygon d = new Polygon();
		for (Pair<Double, Double> pair : points) {
			double x = pair.getKey();
			double y = pair.getValue();
			d.addPoint((int) x, (int) y);
		}
		return d;
	}

	@Override
	public Shape drawRoundedRectangle(double x, double y, double w, double h, boolean right, Color c) {
		roundedrec ++;
		return new Rectangle2D.Double(x, y, w, h);
	}

	@Override
	public Shape drawPrecisionRoundedRectangle(double x1, double y1, double x2, double y2, boolean right, Color c) {
		roundedrec ++;
		return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
	}

	@Override
	public void clearShape(Shape s) {

	}

	@Override
	public Shape drawLine(Vect topLeftCorner, Vect bottomRightCorner, Color col) {
		lines ++;
		return new Line2D.Double(new Point2D.Double(topLeftCorner.x() ,topLeftCorner.y() ), new Point2D.Double(bottomRightCorner.x() , bottomRightCorner.y()));
	}

	@Override
	public Shape[] drawBox(Vect topLeftCorner, Vect topRightCorner, Vect bottomLeftCorner, Vect bottomRightCorner, Color col) {
		lines += 4;
		Line2D l1 = (Line2D) drawLine(topLeftCorner, topRightCorner, Color.red),
				l2 = (Line2D) drawLine(topLeftCorner, bottomLeftCorner, Color.black),
				l3 = (Line2D) drawLine(topRightCorner, bottomRightCorner, Color.green),
				l4 = (Line2D) drawLine(bottomLeftCorner, bottomRightCorner, Color.blue);

		return new Line2D[] {l1, l2, l3, l4};
	}
}