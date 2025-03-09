package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

 */
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

class AsteroidsGame extends Game {
	static int points = 0;
	
	/*
	instance variables to keep track of objects?
	- spaceship
	- arraylist of asteroids (may contain golden asteroid)
	- current boss
	 */
	Spaceship spaceship;
	Polygon background; // may not be necessary...?
	
	public AsteroidsGame() {
		super("Asteroids!", 0, 0);
		this.setFocusable(true);
		this.requestFocus();
		
		// initialize background
		initializeBackground();
		
		// create spaceship, track keystrokes for movement
		initializeSpaceship();

	}
	
	public void initializeBackground() {
		
		// create rectangle background
		Point[] backgroundPoints = {new Point(0,0), new Point(this.width, 0), 
				new Point(this.width, this.height), new Point(0, this.height)};
		Point offset = new Point(0,0);
		double rotation = 0.0;
		this.background = new Polygon(backgroundPoints, offset, rotation);
		
	}
	
	public void initializeSpaceship() {
		
		// create equalateral triangle
		int length = 30;
		int height = (int) (Math.sqrt(3) / 2 * length);
		 Point[] spaceshipPoints = {new Point(0, 0), new Point(length / 2, 
				 height / 5), new Point(length, 0), new Point(length / 2, height)};
		 
		 // calculate offset to place center of spaceship at center of screen
		 int xCenter = this.width / 2 - length / 2, yCenter = this.height / 2 - 
				 height / 2 ; 

		 Point position = new Point(xCenter, yCenter); 
		 double rotation = 180.0;
		 this.spaceship = new Spaceship(spaceshipPoints, position, rotation, 
				 length, height);

		 // give spaceship a way to track keys
		this.addKeyListener(spaceship);
	}

	// continuously calls paint method
	public void paint(Graphics brush) {

		// update points
		brush.setColor(Color.white);
		brush.drawString("Points: " + points, 20, 20);
		
		// update game 
		updateGame(brush);
	}

	/*
	interactable interface will have access to screen size
	each intereactlable poly gon has default method that they can use to check if offscreen
	 */
	
	private void adjustSpaceshipPosition() {
		Point[] spaceshipPoints = this.spaceship.getPoints();
		for (Point p : spaceshipPoints) {
			if (this.background.contains(p)) { 
				return;
			}
			this.spaceship.wrapScreen(this.width, this.height);
		}
	}
	
	// update spaceship (and lasers), asteroids and boss
	private void updateGame(Graphics brush) {
		
		// update background
		brush.setColor(Color.black);
		this.background.paint(brush);
		
		// move and update spaceship (includes lasers)
		brush.setColor(Color.white);
		this.spaceship.move();
		this.spaceship.paint(brush);
		adjustSpaceshipPosition();
		
		
		// move and update all asteroids
		

	}

	public static void main (String[] args) {
		AsteroidsGame a = new AsteroidsGame();
		a.repaint();
	}
}