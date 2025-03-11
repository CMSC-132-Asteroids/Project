package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Arrays;
import java.util.*;

/**
* Spaceship is a class that represents the player and holds all the logic for the player.
* 
* 
* @author Declan Amoako
* @author Allen Bai
* 
*/
public class Spaceship extends Polygon implements KeyListener, Damagable {

	// set can check if multiple keys are pressed concurrently
	private final static Set<Integer> currKeys = new HashSet<Integer>();
	private static double moveVelocity = 0;
	private static double turnVelocity = 0;
	int length, height;
	private int health = 3;
	private int invTicks = 0;
	private boolean invulnerable = false;
	private ArrayList<Laser> lasers;


	private class Laser extends Polygon {
		static double laserVel = 10.0;
		static double laserCD = 0;

		public Laser(Point[] points, Point offset, double rotation) {
			super(points, offset, rotation);
		}

		public void paint(Graphics brush) { 
			
			// change laser's color to be red
			brush.setColor(Color.red);
			
			// loop through Polygon instance variable for points   	
			Point[] laserPoints = this.getPoints();
			int numPoints = laserPoints.length;
			int[] xPoints = new int[numPoints], yPoints = new int[numPoints];

			// create two arrays for x-coords and y-coords
			for (int idx = 0; idx < numPoints; idx++) {
				xPoints[idx] = (int) laserPoints[idx].getX();
				yPoints[idx] = (int) laserPoints[idx].getY();
			}

			// cdraw spaceship using x-coords and y-coords
			brush.fillPolygon(xPoints, yPoints, numPoints);
		}
		
		public void move() {
			
			// calculate change in laser's coordinates using rotation and velocity
			double changeX = laserVel * Math.cos(Math.toRadians(this.rotation - 90));
			double changeY = laserVel * Math.sin(Math.toRadians(this.rotation - 90));

			// change laser's position
			double currX = this.position.getX(), currY = this.position.getY();
			this.position.setX(currX - changeX);
			this.position.setY(currY - changeY);
		}
	}

	// may add more parameters
	public Spaceship(Point[] points, Point offset, double rotation, int length, 
			int height) {
		super (points, offset, rotation);
		this.lasers =  new ArrayList<Laser>();
		this.length = length;
		this.height = height;

	}

	public void paint(Graphics brush, ArrayList<Asteroid> asteroids, Polygon back) {
		
		if(this.invulnerable) {
			this.invTicks--;
			
			if(this.invTicks <= 0) {
				this.invulnerable = false;
			}
		}
		
		if(this.invTicks % 10 != 0 && this.invulnerable) return;

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
		
		// move and update each laser on screen
		for (int i = 0; i < this.lasers.size(); i++) {
			
			// get each laser
			Laser l = this.lasers.get(i);
			boolean collided = false;
			
			// loop through all asteroids
			for (int j = 0; j < asteroids.size(); j++) {
				Asteroid a = asteroids.get(j);
				
				// check if any asteroids collide with laser
				if (a.collides(l)) {
					
					// destroy asteroids and re
					a.destroy();
					asteroids.remove(j);
					collided = true;
					j--;
				}
				
			}
			
			Point[] laserPoint = l.getPoints();
			boolean laserOut = false;
			for(Point p: laserPoint) {
				if(!back.contains(p)) {
					laserOut = true;
					break;
				}
			}
				
			l.move();
			l.paint(brush);
			
			//If the laser is out of screen or has collided remove
			if (laserOut || collided) {
				this.lasers.remove(i);
				i--;
			}
		}
		
		//check if asteroid collided with spaceship
		for(Asteroid cAsteroid: asteroids) {
			if(cAsteroid.collides(this)) {
				if(this.invulnerable) return;
				this.takeHealth(1);
				this.invulnerable = true;
				this.invTicks = 100;
				
			}
		}
	}


	public void move() {
		
		// dampen move and turn velocity (multiply until 0 each frame)
		moveVelocity *= 0.95;
		turnVelocity *= 0.95;
		
		// increase/decrease move velocity based on direction
		if (currKeys.contains(KeyEvent.VK_UP) || currKeys.contains(KeyEvent.VK_W)) {
			moveVelocity += 0.30;
		} else if (currKeys.contains(KeyEvent.VK_DOWN) || currKeys.contains(KeyEvent.VK_S)) {
			moveVelocity -= 0.30;
		}

		// increase/decrease turn velocity based on direction
		if (currKeys.contains(KeyEvent.VK_LEFT) || currKeys.contains(KeyEvent.VK_A)) {
			turnVelocity -= 0.25;
		} else if (currKeys.contains(KeyEvent.VK_RIGHT) || currKeys.contains(KeyEvent.VK_D)) {
			turnVelocity += 0.25;
		}
		
		// shoot a laser
		if (currKeys.contains(KeyEvent.VK_SPACE)) {
			shootLaser();
		}
		
		// always update position using new move and turn velocity
		updatePosition();
	}
	
	private void updatePosition() {
		
		// find current position and get x and y coords
		Point currPos = this.position;
		double currX = currPos.getX(), currY = currPos.getY();

		// change position by move velocity * sin/cos of rotation 
		double changeX = moveVelocity * Math.cos(Math.toRadians(this.rotation - 90)), 
				changeY = moveVelocity * Math.sin(Math.toRadians(this.rotation - 90));

		// lower x/y coords are closer to top left 
		// update position (if move velocity == 0 -> ship won't move)
		currPos.setX(currX - changeX);
		currPos.setY(currY - changeY);
		
		// update rotation (if turn velocity == 0 -> ship won't turn)
		this.rotate((int) turnVelocity);
	}
	
	private void shootLaser() {
		
		// get the point that corresponds to front of spaceship
		Point frontPoint = this.getPoints()[3];
		double laserOriginX = frontPoint.getX();
		double laserOriginY = frontPoint.getY();
		
		// laser size
		double width = 1.75, length = 20;

		Point[] laserPoints = {new Point(laserOriginX, laserOriginY), 
				new Point(laserOriginX + width, laserOriginY), 
				new Point(laserOriginX + width, laserOriginY + length), 
				new Point(laserOriginX, laserOriginY + length)};
		
		// can shoot a laser when CD == 0
		if (Laser.laserCD == 0) {
			
			// create new laser using at front of ship
			Laser objLaser = new Laser(laserPoints, 
					new Point(laserOriginX - width / 2, laserOriginY), 
					this.rotation);
			this.lasers.add(objLaser);
			
			// set cooldown
			Laser.laserCD = 10;
		}
		
		// cooldown decremented every time paint is called
		Laser.laserCD--;
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
	
	public boolean getInv() {
		return this.invulnerable;
	}
	
	public void setInv(boolean val) {
		this.invulnerable = val;
		this.invTicks = 100;
	}

	// add to set of pressed keys
	public void keyPressed(KeyEvent e) {currKeys.add(e.getKeyCode());}

	// remove from set of pressed keys
	public void keyReleased(KeyEvent e) {currKeys.remove(e.getKeyCode());}

	public void keyTyped(KeyEvent e) {}

	@Override
	public int getHealth() {
		return this.health;
	}

	@Override
	public void takeHealth(int amt) {
		this.health -= amt;
	}
}
