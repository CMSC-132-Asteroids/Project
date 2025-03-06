package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

 */
import java.awt.*;
import java.awt.event.*;

class YourGameName extends Game {
	static int counter = 0;
	/*
	instance variables to keep track of objects?
	- spaceship
	- arraylist of asteroids (may contain golden asteroid)
	- current boss
	 */

	public YourGameName() {
		super("YourGameName!",800,600);
		this.setFocusable(true);
		this.requestFocus();
		
		// private method that will call other methods that initialize game
		initializeGame();
	}

	// this method is called by a.repaint() in main continuously
	// a.repaint() also calls update() method in Game class
	
	public void paint(Graphics brush) {
		brush.setColor(Color.black);
		brush.fillRect(0,0,width,height);

		// sample code for printing message for debugging
		// counter is incremented and this message printed
		// each time the canvas is repainted
		counter++;
		brush.setColor(Color.white);
		brush.drawString("Counter is " + counter,10,10);
		
		// method to begin game initialization
		
	}

	// private method to initialize game
	private void initializeGame() {
		
		/*
		- create spaceship
		- 
		 */
	}

	public static void main (String[] args) {
		YourGameName a = new YourGameName();
		a.repaint();
	}
}