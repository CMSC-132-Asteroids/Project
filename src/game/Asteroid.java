package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import game.AsteroidsGame.GameData;




/**
* Asteroid is a class that holds most of the logic needed to display an asteroid.
* 
* @author Declan Amoako
* @author Allen Bai
* 
*/

public class Asteroid extends Polygon {
	
	protected final static double baseVelocity = 5.0;
	
	/**
	* Initializes an asteroid using the Polygon constructor.
	* 
	* @param points
	* @param offset
	* @param rotation
	* 
	*/
    public Asteroid(Point[] points, Point offset, double rotation) {
        super(points, offset, rotation);
    }    

	/**
	* Moves the asteroid based on its velocity and current rotation.
	*/
    protected void move() {
		
		// calculate change in asteroid's coordinates using rotation and velocity
		double changeX = baseVelocity * Math.cos(Math.toRadians(this.rotation));
		double changeY = baseVelocity * Math.sin(Math.toRadians(this.rotation));

		// update asteroid's position
		double currX = this.position.getX(), currY = this.position.getY();
		this.position.setX(currX - changeX);
		this.position.setY(currY - changeY);
	}
 	
	/**
	* Increase or decrease player points based on if the game's boss is visible.
	* 
	* @param boss 
	* 
	*/
    protected void changePoints(Boss boss) {
		
		// decrease points if player hits an asteroid while boss on screen
		if(!boss.getVisibility()) {
			GameData.points += 100;
		} else {
			GameData.points -= 50;
		}
	}
}
