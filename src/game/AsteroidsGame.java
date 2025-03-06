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
	static int point = 0;
	
	/*
	instance variables to keep track of objects?
	- spaceship
	- arraylist of asteroids (may contain golden asteroid)
	- current boss
	 */
	Spaceship s = new Spaceship(new Point[] {new Point(0, 0), new Point(30, 0), new Point(15, 30)}, new Point(400, 300), 180.0);
	
	public AsteroidsGame() {
		super("Asteroids!",800,600);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(s);
		
	}

	// this method is called by a.repaint() in main continuously
	// a.repaint() also calls update() method in Game class
	
	public void paint(Graphics brush) {
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);

		// sample code for printing message for debugging
		// counter is incremented and this message printed
		// each time the canvas is repainted
		brush.setColor(Color.white);
		brush.drawString("Points: " + point ,20,20);
		
		// method to begin game initialization
		updateGame(brush);
	}

	// private method to initialize game
	private void updateGame(Graphics brush) {
		updateSpaceship(brush);
		
	}
	
	private void updateSpaceship(Graphics brush) {
		s.move();
		s.paint(brush);
		
	}

	public static void main (String[] args) {
		AsteroidsGame a = new AsteroidsGame();
		a.repaint();
	}
}