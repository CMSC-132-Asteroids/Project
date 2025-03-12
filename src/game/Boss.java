package game;

import java.awt.Color;
import java.awt.Graphics;

/**
* Boss class that represents the boss that is spawned at a certain threshold.
* 
* 
* @author Declan Amoako
* @author Allen Bai
* 
*/
public class Boss extends Polygon implements Damagable {

	
	//internal use
	private boolean display = false;
	private boolean currentMov = false;
	private int ticks = 0;
	private int health = 5;
	
	
	/**
	* The constructor for Boss
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
    public Boss(Point[] points, Point offset, double rotation) {
        super (points, offset, rotation);
    }
    
    
    /**
    * The paint method for boss.
    * handles drawing the boss.
    * Contains logic to configure when to display it or not.
    * 
    * @param brush is the current graphics context passed down from AsteroidsGame
    * @author Declan Amoako
    * @author Allen Bai
    * 
    */
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
    
    /**
    * The move method for the boss.
    * This method handles reseting the health of the boss after the player defeats them.
    * It also handles generating the randomized movement of the boss.
    * 
    * 
    * @author Declan Amoako
    * @author Allen Bai
    * 
    */
    public void move() {
    	// If boss health is gone
    	if(this.getHealth() <= 0) {
    		this.health = 5;
    	}
    	
    	if(this.ticks >= 50) {
    		this.ticks = 0;
    		this.currentMov = this.determineMovement();
    	}
    	
    	// 
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
    
    /**
    * Getter method for display.
    * 
    * 
    * @author Declan Amoako
    * @author Allen Bai
    * 
    */
    public boolean getDisplay() {
    	return this.display;
    }
    
    /**
    * Toggle method for display.
    * 
    * 
    * @author Declan Amoako
    * @author Allen Bai
    * 
    */
    public void setDisplay() {
    	this.display = !this.display;
    }
    
    /**
    * A private method that is used to determine the movement of the boss.
    * Contains a Math.random() call which determines if to return true or false
    * 
    * @return a boolean represeting left or right (true for right and false for left)
    * @author Declan Amoako
    * @author Allen Bai
    * 
    */
    private boolean determineMovement() {
    	
    	if(Math.random() > 0.50) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * A private method that is used to determine the movement of the boss.
     * Contains a Math.random() call which determines if to return true or false
     * 
     * @param the width parameter is the max width before wrap
     * @param the height parameter is the max height before wrap
     * @author Declan Amoako
     * @author Allen Bai
     * 
     */
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
