package game;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.Arrays;
import java.util.*;
public class Spaceship extends Polygon implements Collidable, KeyListener {
	
	/*
	1. constructor to create spaceship
	- triangle (for now)
	- position = center of screen
	- rotation = 0.0 (or rotate 90 degrees to point in certain direction)
	2. implement collidable methods
	- use Polygon's .contains() method to check if its points lies within another
	collidable object
	- (other collidable object will also check if they're points are within THIS
	object)
	- loss a life method on spaceship collision
	3. shoot a laser
	- create nested class
	- method creates instance of laser and adds arraylist of lasers
	- lasers will also check for collidable with asteroids/boss
	- lasers start with orientation of spaceship and move toward that direction
	4. rotation (and possibly movement later)
	- method to take in input and rotate spaceship accordingly (does input rotation need to != 0)?
	
	 */
	
	// set can check if multiple keys are pressed concurrently
	final static Set<Integer> currKeys = new HashSet<Integer>();
	final static double velocity = 3.0;
	Direction direction;
	ArrayList<Laser> lasers;
	
	private enum Direction {
		UP, 
		DOWN, 
		NONE
	}
	
	
	private class Laser extends Polygon {
		int laserSpeed = 0;
		
		public Laser(Point[] points, Point offset, double rotation) {
			super(points, offset, rotation);
		}
		
		public void paint(Graphics brush) { 
			//Paint here
		}
	}
 	
	// may add more parameters
    public Spaceship(Point[] points, Point offset, double rotation) {
        super (points, offset, rotation);
        this.direction = Direction.NONE;

        
    }
    
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
    	
    	// cdraw spaceship using x-coords and y-coords
    	brush.fillPolygon(xPoints, yPoints, numPoints);
    }

    public void move() {

    	// find current position and get x and y coords
    	Point currPos = this.position;
    	double currX = currPos.getX(), currY = currPos.getY();
    	
    	// change position by velocity * sin/cos of rotation 
    	double changeX = velocity * Math.cos(Math.toRadians(this.rotation - 90)), 
    			changeY = velocity * Math.sin(Math.toRadians(this.rotation - 90));

    	// multiply x/y change by 1 or -1 depending on direction
    	double movementFactor;
    	if (currKeys.contains(KeyEvent.VK_UP) || currKeys.contains(KeyEvent.VK_W)) {
    		movementFactor = 1;
    	} else if (currKeys.contains(KeyEvent.VK_DOWN) || currKeys.contains(KeyEvent.VK_S)) {
    		movementFactor = -1;
    	} else {
    		movementFactor = 0;
    	}
    	
    	if (currKeys.contains(KeyEvent.VK_LEFT) || currKeys.contains(KeyEvent.VK_A)) {
    		this.rotate(-2);
    	} else if (currKeys.contains(KeyEvent.VK_RIGHT) || currKeys.contains(KeyEvent.VK_D)) {
    		this.rotate(2);
    	}
    	
    	if(currKeys.contains(KeyEvent.VK_SPACE)) {
    		Point[] laserPoints = {new Point(currPos.getX(), currPos.getY()), new Point(currPos.getX() - 20, currPos.getY() - 20)};
    		Laser objLaser = new Laser(laserPoints, new Point(0,0), 0);
    		
    		this.lasers.add(objLaser);
    		//Spawn new laser and add to laser array
    		
    	}
    	
    	// lower x/y coords are closer to top left
    	currPos.setX(currX - changeX * movementFactor);
    	currPos.setY(currY - changeY * movementFactor);
       		
    }

    public void keyPressed(KeyEvent e) {
    	
    	// add to set of pressed keys
    	currKeys.add(e.getKeyCode());
    }
    
    public void keyReleased(KeyEvent e) {
    	
    	// remove from set of pressed keys
    	currKeys.remove(e.getKeyCode());
    }
    
    public void keyTyped(KeyEvent e) {
    	
    }
    
	@Override
	public boolean isCollision(Collidable other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void collide() {
		// TODO Auto-generated method stub
		
	}
    
}
