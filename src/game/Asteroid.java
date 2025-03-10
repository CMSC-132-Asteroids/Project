package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;


public class Asteroid extends Polygon {
	
	final static double baseVelocity = 5.0;
	
	/*
	1. generate instance variables
	- generate shape method (start off with circle > may generate random points) 
	- generate original position/offset (at edge of screen > offscreen if time?)
	- rotation should face toward center of screen and move toward player
	- (two rotation variable to keep real rotation and another to rotate graphically while it moves?)
	2. collidable interface methods
	- collides with boss
	- collides with lasers
	- collides with other asteroids
	- method for asteroid to destroy itself on contact?
	3. method for asteroid to move (and possibly spin)
	 */
	
	
    public Asteroid(Point[] points, Point offset, double rotation) {
        super(points, offset, rotation);
    }
    

	public void move() {

		
		// calculate change in laser's coordinates using rotation and velocity
		double changeX = baseVelocity * Math.cos(Math.toRadians(this.rotation));
		double changeY = baseVelocity * Math.sin(Math.toRadians(this.rotation));

		// change laser's position
		double currX = this.position.getX(), currY = this.position.getY();
		this.position.setX(currX - changeX);
		this.position.setY(currY - changeY);
	}
	
	@Override
    public void paint(Graphics brush) {
    	
    	// access Polygon's shape instance variable 
    	Point[] spaceshipPoints = this.getPoints();
    	
    	// loop through Polygon instance variable for points   	
    	int numPoints = spaceshipPoints.length;
    	int[] xPoints = new int[numPoints], yPoints = new int[numPoints];
    	
    	// create two arrays for x-coords and y-coords
    	for (int idx = 0; idx < numPoints; idx++) {
    		xPoints[idx] = (int) spaceshipPoints[idx].getX();
    		yPoints[idx] = (int) spaceshipPoints[idx].getY();
    	}
    	
    	// draw spaceship using x-coords and y-coords
    	brush.fillPolygon(xPoints, yPoints, numPoints);
    }
}
