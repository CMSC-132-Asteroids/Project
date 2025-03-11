package game;

import java.awt.Color;
import java.awt.Graphics;

public class Boss extends Polygon implements Damagable {

	// default constructor > may add more attributes later
	/*
	level? > determines HP/speed/attributes of boss
	
	(if time at end? > random object > enum class to hold different boss shapes 
	that correlate to different numbers) 
	 */
	
	//Starts off as false since you dont want the boss displayed yet
	private boolean display = false;
	private boolean currentMov = false;
	private int ticks = 0;
	private int health = 5;
	
    public Boss(Point[] points, Point offset, double rotation) {
        super (points, offset, rotation);
    }
    
    public void paint(Graphics brush) { 
    	// change laser's color to be red
    	
    	if(!this.display) return;
    	
    	brush.setColor(Color.red);
    				
    	// loop through Polygon instance variable for points   	
    	Point[] bossPoints = this.getPoints();
    	int numPoints = bossPoints.length;
    	int[] xPoints = new int[numPoints], yPoints = new int[numPoints];

    	// create two arrays for x-coords and y-coords
    	for (int idx = 0; idx < numPoints; idx++) {
    			xPoints[idx] = (int) bossPoints[idx].getX();
    			yPoints[idx] = (int) bossPoints[idx].getY();
    	}

    	// cdraw spaceship using x-coords and y-coords
    	brush.fillPolygon(xPoints, yPoints, numPoints);
    }
    
    public void move() {
    	if(this.ticks >= 50) {
    		this.ticks = 0;
    		this.currentMov = this.determineMovement();
    	}
    	
    	
    	double currX = this.position.getX();
    	
    	double changeX = Math.floor(Math.random() * 10);
    	
    	//Move right if true else move left
    	if(this.currentMov) {		
    		this.position.setX(currX + changeX);
    	} else  {
    		this.position.setX(currX - changeX);
    	}
    	
    	this.ticks++;
    }
    
    public boolean getDisplay() {
    	return this.display;
    }
    
    public void setDisplay() {
    	this.display = !this.display;
    }
    
    private boolean determineMovement() {
    	
    	if(Math.random() > 0.50) {
    		return true;
    	}
    	
    	return false;
    }
    
    public void wrapScreen(int width, int height) {
		
		// find current position
		Point position = this.position;
		double currX = position.getX();
		double currY = position.getY();

		// check all directions of screen adjust position accordingly
		if (currX <= 0) {
			position.setX(currX + width);
		} else if (currX > width) {
			position.setX(currX - width);
		} else if (currY <= 0) {
			position.setY(currY + height);
		} else if (currY > height) {
			position.setY(currY - height);
		}
	}

	@Override
	public int getHealth() {
		return this.health;
	}

	@Override
	public void takeHealth(int amt) {
		this.health -= amt;
	}
}
