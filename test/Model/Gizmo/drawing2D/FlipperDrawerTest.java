package Model.Gizmo.drawing2D;


import Controller.DrawController.iDrawerTest;
import Model.GameConstraints;
import org.junit.Before;
import org.junit.Test;
import physics.Circle;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class FlipperDrawerTest {
	FlipperDrawer rightFlipper, leftFlipper;
	iDrawerTest drawer;
	@Before
	public void setUp() {
		rightFlipper = new FlipperDrawer(1,1,0,0,0,true);
		leftFlipper = new FlipperDrawer(1,1,0,0,0,false);
		drawer = new iDrawerTest();
	}

	@Test
	public void drawRight() throws Exception {
		rightFlipper.draw(drawer, Color.BLACK);
		assertEquals(drawer.circle, 2);
		assertEquals(drawer.lines, 1);
		assertEquals(drawer.rectangles,0);
		assertEquals(drawer.strings,0);
		assertEquals(drawer.roundedrec,0);
		assertEquals(drawer.polygon,0);
	}

	@Test
	public void drawLeft() throws Exception {
		rightFlipper.draw(drawer, Color.BLACK);
		rightFlipper.draw(drawer, Color.BLACK);
		assertEquals(drawer.circle, 4);
		assertEquals(drawer.lines, 2);
		assertEquals(drawer.rectangles,0);
		assertEquals(drawer.strings,0);
		assertEquals(drawer.roundedrec,0);
		assertEquals(drawer.polygon,0);
	}

	@Test
	public void rotateFlipperRight() throws Exception {
		List<Circle> circles = rightFlipper.physicsShape.getCircles();
		int dir = 1;
		for (int i = 0; i < 100; i ++)
			dir = rightFlipper.rotateFlipper(10, dir,new GameConstraints());
		assertTrue(compaireCircles(circles, rightFlipper.physicsShape.getCircles(), dir));
	}


	@Test
	public void rotateFlipperLeft() throws Exception {
		List<Circle> circles = leftFlipper.physicsShape.getCircles();
		int dir = 1;
		for (int i = 0; i < 100; i ++)
			dir = leftFlipper.rotateFlipper(10, dir, new GameConstraints() );
		assertTrue(compaireCircles(circles, leftFlipper.physicsShape.getCircles(), dir));
	}



	public boolean compaireCircles(List<Circle> a, List<Circle> b, int dir){
		if( a.get(0).getCenter().x() == b.get(0).getCenter().x() && a.get(0).getCenter().y() == b.get(0).getCenter().y()) {
			////System.out.println.out.println((a.get(1).getCenter().x() + ":"  + b.get(1).getCenter().x()));
			if(a.get(1).getCenter().x() == b.get(1).getCenter().x()) {
				return dir == 0;
			} else if (a.get(1).getCenter().x() < b.get(1).getCenter().x()) {
				return dir == -1;
			} else {
				return dir == 1;
			}
		} else {
			return false;
		}
	}



	@Test
	public void copyRight() throws Exception {
		FlipperDrawer copy = (FlipperDrawer) rightFlipper.copy();
		assertEquals(rightFlipper.location.y(), copy.location.y(), 0);
		assertEquals(rightFlipper.location.x(), copy.location.x(), 0);
		assertEquals(rightFlipper.location.w(), copy.location.w(), 0);
		assertEquals(rightFlipper.location.h(), copy.location.h(), 0);
		assertEquals(rightFlipper.location.r(), copy.location.r(), 0);
		assertEquals(rightFlipper.isRight, copy.isRight);
	}

	@Test
	public void copyLeft() throws Exception {
		FlipperDrawer copy = (FlipperDrawer) leftFlipper.copy();
		assertEquals(leftFlipper.location.x(), copy.location.x(), 0);
		assertEquals(leftFlipper.location.y(), copy.location.y(), 0);
		assertEquals(leftFlipper.location.w(), copy.location.w(), 0);
		assertEquals(leftFlipper.location.r(), copy.location.r(), 0);
		assertEquals(leftFlipper.location.h(), copy.location.h(), 0);
		assertEquals(leftFlipper.isRight, copy.isRight);
	}
}