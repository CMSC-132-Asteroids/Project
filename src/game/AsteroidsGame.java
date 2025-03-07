package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

 */
import java.awt.*;
import java.awt.event.*;

class AsteroidsGame extends Game {
	static int points = 0;
	
	/*
	instance variables to keep track of objects?
	- spaceship
	- arraylist of asteroids (may contain golden asteroid)
	- current boss
	 */
	Spaceship spaceship;
	
	public AsteroidsGame() {
		super("Asteroids!",800,600);
		this.setFocusable(true);
		this.requestFocus();
		
		// create spaceship, track keystrokes for movement
		initializeSpaceship();
		this.addKeyListener(spaceship);
	}
	
	public void initializeSpaceship() {
		
		// create equalateral triangle
		int length = 30;
		int height = (int) (Math.sqrt(3) / 2 * length);
		 Point[] spaceshipPoints = {new Point(0, 0), new Point(length/2, height/5),new Point(length, 0), new 
				 Point(length/2, height)};
		 
		 // calculate offset to place center of spaceship at center of screen
		 int xCenter = this.width / 2 - length / 2, yCenter = this.height / 2 - 
				 height / 2; 
		 System.out.println(xCenter);
		 System.out.println(yCenter);

		 Point position = new Point(xCenter, yCenter); 
		 double rotation = 180.0;
		 this.spaceship = new Spaceship(spaceshipPoints, position, rotation);
	}

	// continuously calls paint method
	public void paint(Graphics brush) {
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);

		// update points
		brush.setColor(Color.white);
		brush.drawString("Points: " + points, 20, 20);
		
		// update game 
		updateGame(brush);
	}

	// update spaceship (and lasers), asteroids and boss
	private void updateGame(Graphics brush) {
		
		// move and update spaceship (includes lasers)
		spaceship.move();
		spaceship.paint(brush);
		
		// move and update all asteroids
		
	}

	public static void main (String[] args) {
		AsteroidsGame a = new AsteroidsGame();
		a.repaint();
	}
}