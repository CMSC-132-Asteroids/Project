package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Arrays;
import java.util.*;

// FINAL

/**
 * Spaceship is a class that represents the player and holds all the logic for 
 * the player.
 * 
 * @author Declan Amoako
 * @author Allen Bai
 * 
 */
public class Spaceship extends Polygon implements KeyListener, Damagable {

	// HashSet can check if multiple keys are pressed concurrently
	private final static Set<Integer> currKeys = new HashSet<Integer>();
	private static double moveVelocity = 0;
	private static double turnVelocity = 0;
	private int health = 3;
	private int invTicks = 0;
	private boolean invulnerable = false;
	private ArrayList<Laser> lasers;


	/**
	 * Laser is an inner class of Spaceship have their own move and paint 
	 * methods. They can interact with the boss and asteroids.
	 * 
	 * @author Declan Amoako
	 * @author Allen Bai
	 * 
	 */
	private class Laser extends Polygon {
		private static double laserVelocity = 10.0;
		private static double laserCD = 0;

		/**
		 * Initializes a new Laser using Polygon constructor.
		 * 
		 * @param points
		 * @param offset
		 * @param roation
		 * 
		 */
		public Laser(Point[] points, Point offset, double rotation) {
			super(points, offset, rotation);
		}

		/**
		 * Updates a laser's position based on the spaceship's initial rotation
		 * at the time laser was shot and the laser's velocity.
		 * 
		 */
		private void move() {

			// calculate change in laser's coordinates using rotation and velocity
			double changeX = laserVelocity * 
					Math.cos(Math.toRadians(this.rotation - 90));
			double changeY = laserVelocity * 
					Math.sin(Math.toRadians(this.rotation - 90));

			// change laser's position
			double currX = this.position.getX(), currY = this.position.getY();
			this.position.setX(currX - changeX);
			this.position.setY(currY - changeY);
		}
	}

	/**
	 * Initializes a Spaceship using the Polygon Constructor
	 * 
	 * @param points
	 * @param offset
	 * @param rotation
	 * 
	 */
	public Spaceship(Point[] points, Point offset, double rotation) {
		super(points, offset, rotation);
		this.lasers = new ArrayList<Laser>();

	}

	/**
	 * Updates the spaceship and lasers on screen, handling for their collisions
	 * with either the boss or asteroids.
	 * 
	 * @param brush
	 * @param asteroids list of current asteroids on screen
	 * @param background background of the current game
	 * @param boss current boss
	 * 
	 */
	public void paint(Graphics brush, ArrayList<Asteroid> asteroids, 
			Polygon background, Boss boss) {

		// checks if spaceship is invulnerable
		if (this.invulnerable) {
			this.invTicks--;
			if(this.invTicks <= 0) {
				this.invulnerable = false;
			}
		}

		// creates flashing effect for spaceship
		if (this.invTicks % 10 != 0 && this.invulnerable) return;

		// paints the spaceship using the Polygon's paint method
		super.paint(brush);

		// move and update each laser on screen
		brush.setColor(Color.red);
		for (int i = 0; i < this.lasers.size(); i++) {

			// get each laser
			Laser l = this.lasers.get(i);
			boolean collided = false;

			// loop through all asteroids
			for (int j = 0; j < asteroids.size(); j++) {
				Asteroid a = asteroids.get(j);

				// check if any asteroids collide with laser
				if (a.collides(l)) {

					// update points if lasers destroy any asteroids
					a.changePoints(boss);
					asteroids.remove(j);
					collided = true;
					j--;
				}
			}

			// check if laser ccollides with boss
			if (boss.getVisibility()) {
				if(boss.collides(l)) {
					boss.getHit();
					collided = true;
				}
			}

			l.move();
			l.paint(brush);

			// remove lasers that are off screen or that have collided
			if (!background.collides(l) || collided) {
				this.lasers.remove(i);
				i--;
			}
		}

		// decrease lives and make spaceship invulnerable if hit by asteroid
		for (Asteroid asteroid: asteroids) {
			if (asteroid.collides(this)) {
				if(this.invulnerable) return;
				this.getHit();
				this.invulnerable = true;
				this.invTicks = 100;	
			}
		}
	}

	/**
	 * Updates movement and turning velocity of spacehship based on keystrokes.
	 * 
	 */
	protected void takeInput() {

		// dampen move and turn velocity (multiply until 0) each frame
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
		move();
	}

	/**
	 * Update spacesihp's position based on current velocity. Move and turn
	 * velocity changed by @see takeInput();
	 * 
	 */
	private void move() {

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

	/**
	 * Creates a new laser and adds it to spaceship's current lasers. There is a
	 * cooldown on how frequently lasers can be shot.
	 * 
	 */
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

	/**
	 * Get the spaceship's invulnerability status.
	 * 
	 * @return boolean that represents whether spaceship is invulnerable
	 */
	protected boolean getInv() {
		return this.invulnerable;
	}

	/**
	 * Set the spaceship's invulnerability status.
	 * 
	 * @param val boolean represent whether spaceship should invulnerable or not
	 */
	protected void setInv(boolean val) {
		this.invulnerable = val;
		this.invTicks = 100;
	}

	/**
	 * Sets health of spaceship.
	 * 
	 * @param health new health of spaceship
	 * 
	 */
	protected void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Gets health of spaceship.
	 * 
	 * @return int representng the current lives that the player has
	 * 
	 */
	@Override
	public int getHealth() {
		return this.health;
	}

	/**
	 * Decrements the current health of the spaceship.
	 * 
	 */
	@Override
	public void getHit() {
		this.health--;

	}


	/**
	 * Takes in keystrokes and adds them to set of keys being pressed.
	 * 
	 * @param e current key beng pressed
	 * 
	 */
	public void keyPressed(KeyEvent e) {currKeys.add(e.getKeyCode());}

	/**
	 * Takes in keystrokes and removes them from set of keys being pressed.
	 * 
	 * @param e current key beng released
	 * 
	 */
	public void keyReleased(KeyEvent e) {currKeys.remove(e.getKeyCode());}

	/**
	 * Method that checks when keys are typed (unutilized).
	 * 
	 * @param e current key beng typed
	 * 
	 */
	public void keyTyped(KeyEvent e) {}
}

