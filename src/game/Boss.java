package game;

import java.awt.Color;
import java.awt.Graphics;

/**
* Boss class represents the red square boss spawned each time player reaches
* a certain number of points.
* 
* @author Declan Amoako
* @author Allen Bai
* 
*/
public class Boss extends Polygon implements Damagable {

	private boolean display = false;
	private boolean movingLeft = false;
	private int ticks = 0;
	private int health = 5;
	
	
	/**
	* Initializes a boss using the Polygon constructor.
	* 
	* @param points
	* @param offset
	* @param rotation
	* 
	*/
    public Boss(Point[] points, Point offset, double rotation) {
        super (points, offset, rotation);
    }
    
    
    /**
    * Changes brush color to red and paints the boss if its visible.
    * 
    * @param brush 
    * 
    */
    public void paint(Graphics brush) { 
    	
    	// check whether to update boss depending on if its being displayed
    	if (!this.display) return;
    	
    	// change laser's color to be red
    	brush.setColor(Color.red);
    	super.paint(brush);
    }
    
    /**
    * Randomly generates the boss' next movement. The boss' health is reset by
    * this method after a player defeats it.
    * 
    */
    protected void move() {
    	
    	// reset boss health if defeated
    	if (this.getHealth() <= 0) {this.health = 5;}
    	
    	// changes the boss' movement ever
    	if (this.ticks >= 50) {
    		this.ticks = 0;
    		this.movingLeft = (Math.random() > .50) ? true : false;
    	}
    	
    	// get boss' curernt position and calculate horizantal change in position
    	double currX = this.position.getX();
    	double changeX = Math.floor(Math.random() * 10);
    	
    	// update boss based on if moving left or right
    	if (this.movingLeft) {		
    		this.position.setX(currX - changeX);
    	} else  {
    		this.position.setX(currX + changeX);
    	}
    	
    	this.ticks++;
    }
    
    /**
    * Get the boss' current visibility.
    * 
    * @return a boolean that represents whether boss can be seen
    * 
    */
    protected boolean getVisibility() {
    	return this.display;
    }
    
    /**
    * Toggle the boss' visibility.
    * 
    */
    protected void changeVisibility() {
    	this.display = !this.display;
    }

    /**
    * Get the boss' current health.
    * 
    * @return an integer that represents remaining HP of boss
    */
	@Override
	public int getHealth() {
		return this.health;
	}

    /**
    * Decrement boss' current health by 1.
    * 
    */
	@Override
	public void getHit() {
		this.health--;
	}
}
