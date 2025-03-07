package game;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.Arrays;

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
	
	static double velocity = 1.5;
	Direction direction;
	Laser[] lasers;
	
	private enum Direction {
		UP, 
		DOWN, 
		NONE
	}
	
	private class Laser extends Polygon {
		int laserSpeed = 0;
		
		public Laser(Point[] points, Point offset, double rotation, Point p) {
			super(points, offset, rotation);
		}
	}
 	
	// may add more parameters
    public Spaceship(Point[] points, Point offset, double rotation) {
        super (points, offset, rotation);
        this.direction = Direction.NONE;

        
    }
    
    public void paint(Graphics brush) {
    	
		int length = 30;
		int height = (int) (Math.sqrt(3) / 2 * length);
		 Point[] spaceshipPoint = {new Point(0, 0), new Point(length, 0), new 
				 Point(length/2, height)};
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

    	// find current position
    	Point currPos = this.position;
    	
    	// increment
       //	currPos.setX(currPos.getX() + 1.5);
       		
    }
    
    private void updatePoints() {
    	Point[] spaceshipPoints = this.getPoints();
    	
    	for (int i = 0; i < spaceshipPoints.length; i++) {
    		spaceshipPoints[i].setX(500);// Math.cos(Math.toRadians(this.rotation)));
    		//System.out.println(p.getX());
    	}
    	/*
       	for (Point p : this.getPoints()) {
    		p.setY(p.getY() + -10 * Math.sin(Math.toRadians(this.rotation)));
    	}
    	*/
    	
    }
    
    public void keyPressed(KeyEvent e) {
    	
    	System.out.println(this.direction);
    	int currKey = e.getKeyCode();
   		System.out.println(currKey);
    	
    	
    	if (currKey == 38) {
    		this.direction = Direction.UP;
    	} 
    	
    	if (currKey == 40) {
    		this.direction = Direction.DOWN;
    	} 
    	
    }
    
    public void keyReleased(KeyEvent e) {
    	//this.direction = Direction.NONE;
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
