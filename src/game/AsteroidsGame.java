package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

 */
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import game.Point;


/**
* AsteroidsGame is the class that holds the main game event loop.
* It holds all the objects in the game, and decides their behavior.
* It extends the Abstract class which provides us all the functionality needed to display a window.
* 
* 
* @author Declan Amoako
* @author Allen Bai
* 
*/
class AsteroidsGame extends Game {
	
	//Inner class used to keep track of all Game Data
	public static class GameData {
		public static int points = 0;
		public static int startTimer = 100;
		public static int asteroidTimer = 0;
		public static double asteroidSpawnFactor = 1;
		public static boolean goldenAsteroid = false;
		public static boolean gameOver = false;
		public static int bossThreshold = 100;
	}

	private Spaceship spaceship;
	private Boss boss;
	private Polygon background;
	private ArrayList<Asteroid> asteroids = new ArrayList<>();

	
	/**
	* Default constructor for AsteroidGame
	* It initializes all the main game objects.
	* These objects are:
	* background
	* spaceship
	* Boss
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	public AsteroidsGame() {
		super("Asteroids!", 0, 0);
		this.setFocusable(true);
		this.requestFocus();

		// initialize background
		initializeBackground();

		// create spaceship, track keystrokes for movement
		initializeSpaceship();
		
		//Creates the boss but does not display the boss
		initializeBoss();

	}
	
	/**
	* Initializes the background.
	* The background is used to determine when an object is off screen.
	* It essentially represents the screen.
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	public void initializeBackground() {

		// create rectangle background
		Point[] backgroundPoints = {new Point(0, 0), new Point(this.width, 0), 
				new Point(this.width, this.height), new Point(0, this.height)};
		Point offset = new Point(0, 0);
		double rotation = 0.0;
		this.background = new Polygon(backgroundPoints, offset, rotation);
	}
	
	
	/**
	* Initializes the Spaceship.
	* The Spaceship represents the player.
	* The player can move using WASD or the arrow keys.
	* The player can shoot using SPACEBAR.
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
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
		this.spaceship = new Spaceship(spaceshipPoints, position, rotation);

		// give spaceship a way to track keys
		this.addKeyListener(spaceship);
	}
	
	/**
	* Initializes the Boss.
	* The Boss is the hardest objective for the game that only occurs at certain points.
	* The Boss's only role is to make the game difficult for the player.
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	public void initializeBoss() {
		int length = 50;
		int height = (int) (Math.sqrt(3) / 2 * length);
		Point[] bossPoints = {new Point(0, 0), new Point(200, 0),  new Point(200, 200), new Point(0, 200)};

		// calculate offset to place center of spaceship at center of screen
		int xCenter = this.width / 2 - length / 2, yCenter = this.height / 5 - 
				height / 2 ; 

		Point position = new Point(xCenter, yCenter); 
		
		this.boss = new Boss(bossPoints, position, 0);
	}
	
	public void rotateBoss() {

		// get spaceship's position
		Point spaceshipPos = this.spaceship.position;
		double spaceshipX = spaceshipPos.getX();
		double spaceshipY = spaceshipPos.getY();

		// calculate angle of asteroid based on spaceship and spawn position
		//RotationCalculator topLeft = (())
		double bossX = this.boss.position.getX();
		double bossY = this.boss.position.getY();
		double rotation = Math.toDegrees(Math.acos(Math.abs((bossX - spaceshipX)) 
				/ (Math.sqrt(Math.pow(Math.abs(bossX - spaceshipX), 2) + 
						Math.pow(spaceshipY, 2)))));
		
		// calculate different rotations based on spawn position
		RotationCalculator bottomLeft = ((angle) -> (0));
		RotationCalculator topLeft = ((angle) -> (0));
		RotationCalculator bottomRight = ((angle) -> (0));
		RotationCalculator r1 = ((angle) -> (-180 - angle));
		
		if (bossX > spaceshipX) {
			rotation = (bossY < spaceshipY) ? r1.getRotation(rotation) : 
				bottomLeft.getRotation(rotation);
		} else {
			rotation = (bossY < spaceshipY) ? bottomLeft.getRotation(rotation) : 
				bottomRight.getRotation(rotation);
		}
		this.boss.setRotation(rotation);
	}
	
	
	/**
	* Handles the spawn system for the asteroid.
	* 
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	public void spawnAsteroid() {

		// check CD for asteroid
		if (GameData.asteroidTimer == 0) {
			
			// randomize 
			Random random = new Random();
			int numPoints = random.nextInt(3,7);
			int scale = random.nextInt(25, 75); 
			Point[] asteroidPoints = new Point[numPoints];
			int spawnLocation = random.nextInt(0, 2);
			int spawnX, spawnY; 
			double rotation;
			double velocity;
			
			// get spaceship's position
			Point spaceshipPos = this.spaceship.position;
			double spaceshipX = spaceshipPos.getX();
			double spaceshipY = spaceshipPos.getY();

			// calculate angle of asteroid based on spaceship and spawn position
			//RotationCalculator topLeft = (())
			spawnX = random.nextInt(-scale, this.width + scale);
			spawnY = (spawnLocation == 0) ? -scale : this.height - scale * 2;
			rotation = Math.toDegrees(Math.acos(Math.abs((spawnX - spaceshipX)) 
					/ (Math.sqrt(Math.pow(Math.abs(spawnX - spaceshipX), 2) + 
							Math.pow(spaceshipY, 2)))));
			
			// calculate different rotations based on spawn position
			RotationCalculator topRight = ((angle) -> (-angle));
			RotationCalculator topLeft = ((angle) -> (180 + angle));
			RotationCalculator bottomRight = ((angle) -> (angle));
			RotationCalculator bottomLeft = ((angle) -> (180 - angle));
			
			if (spawnX > spaceshipX) {
				rotation = (spawnY < 0) ? topRight.getRotation(rotation) : 
					bottomRight.getRotation(rotation);
			} else {
				rotation = (spawnY < 0) ? topLeft.getRotation(rotation) : 
					bottomLeft.getRotation(rotation);
			}
			rotation += random.nextInt(-20, 20);


			// create new point to use as offset
			Point spawnPoint = new Point(spawnX, spawnY);

			// generate points 
			for (int point = 0; point < numPoints; point++) {
				int currAngle = point * 360 / numPoints;
				double x = scale * Math.cos(Math.toRadians(currAngle));
				double y = scale * Math.sin(Math.toRadians(currAngle));
				asteroidPoints[point] = new Point(x, y);
			}

			// add new asteroid to list of asteroids 
			if (GameData.goldenAsteroid == false && GameData.points >= 1000) {

				GameData.goldenAsteroid = true;
				scale = 20;
				numPoints = random.nextInt(5, 12) * 2;
				asteroidPoints = new Point[numPoints];
				
				// generate points 
				for (int point = 0; point < numPoints; point++) {
					int currAngle = point * 360 / numPoints;
					double x = scale * Math.cos(Math.toRadians(currAngle));
					double y = scale * Math.sin(Math.toRadians(currAngle));
					if (point % 2 == 0) {
						x /= 1.5;
						y /= 1.5;
					}
					asteroidPoints[point] = new Point(x, y);
				}
				//spawnPoint = new Point(500, 500);
				this.asteroids.add(new Asteroid(asteroidPoints, spawnPoint, rotation) {		
					
					@Override
					public void move() {

						// calculate change in laser's coordinates using rotation and velocity
						double changeX = baseVelocity * 2 * Math.cos(Math.toRadians(this.rotation));
						double changeY = baseVelocity * 2 * Math.sin(Math.toRadians(this.rotation));

						// change laser's position
						double currX = this.position.getX(), currY = this.position.getY();
						this.position.setX(currX - changeX);
						this.position.setY(currY - changeY);
					}
					@Override
					public void paint(Graphics brush) {
						brush.setColor(Color.yellow);

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
						brush.setColor(new Color(180, 160, 140));
				    }
					
					public void destroy() {
						GameData.points += 1000;
						GameData.goldenAsteroid = false;
					}
				});
			} else {
				this.asteroids.add(new Asteroid(asteroidPoints, spawnPoint, rotation));
			}

			
			// add random CD to timer
			GameData.asteroidTimer += GameData.asteroidSpawnFactor * random.nextInt(50, 100);
			if (GameData.asteroidSpawnFactor > 0.1) {
				GameData.asteroidSpawnFactor *= .995;
			}
		}
		GameData.asteroidTimer--;
	}
	
	
	/**
	* This is the main game loop.
	* The main game loop updates the needed variables.
	* It also calls the move and paint methods of the game objects.
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	public void updateGame(Graphics brush) {

		// decrement time until first asteroid spawn
		if (GameData.startTimer > 0) {
			GameData.startTimer--;
		} else {
			spawnAsteroid();
		}

		// update background
		brush.setColor(Color.black);
		this.background.paint(brush);
		
		// update points
		brush.setColor(Color.white);
		brush.setFont(new Font("Impact", Font.PLAIN, 30));
		brush.drawString("Points: " + GameData.points, 20, this.height - 90);
		
		brush.setColor(Color.red);
		brush.setFont(new Font("Helvetica", Font.PLAIN, 30));
		
		
		if(this.spaceship.collides(this.boss) && !this.spaceship.getInv() && this.boss.getDisplay()) {
			this.spaceship.takeHealth(1);
			this.spaceship.setInv(true);
		}
		
		
		switch (this.spaceship.getHealth()) {
			case 3:
				brush.drawString("❤️❤️❤️", 20, this.height/20);
				break;
			case 2:
				brush.drawString("❤️❤️", 20, this.height/20);
				break;
			case 1:
				brush.drawString("❤️", 20, this.height/20);
				break;
			default:
				brush.drawString("", 20, this.height/20);
				GameData.gameOver = true;
				break;
		}

		// move and update spaceship (includes lasers)
		brush.setColor(Color.white);
		this.spaceship.takeInput();
		this.spaceship.paint(brush, this.asteroids, this.background, this.boss);
		
		
		//If ThresHold reached
		if(GameData.points == GameData.bossThreshold && !this.boss.getDisplay()) {
			this.boss.setDisplay();
			GameData.bossThreshold *= 2;
		}
		
		//If the boss exists and is displayable do this
		if(this.boss != null && this.boss.getDisplay()) {
			//rotateBoss();
			
			brush.setColor(Color.red);
			brush.setFont(new Font("Helvetica", Font.PLAIN, 30));
			//Boss Health
			switch (this.boss.getHealth()) {
			case 5:
				brush.drawString("❤️❤️❤️❤️❤️", 20, this.height/10);
				break;
			case 4:
				brush.drawString("❤️❤️❤️❤️", 20, this.height/10);
				break;
			case 3:
				brush.drawString("❤️❤️❤️", 20, this.height/10);
				break;
			case 2:
				brush.drawString("❤️❤️", 20, this.height/10);
				break;
			case 1:
				brush.drawString("❤️", 20, this.height/10);
				break;
			default:
				brush.drawString("", 20, this.height/20);
				this.spaceship.setHealth(3);
				this.boss.setDisplay();
				break;
			}
			
			this.boss.move();
			this.boss.paint(brush);
			this.boss.wrapScreen(this.width, this.height);
			
		}

		// adjustElementPositions();
		spaceship.wrapScreen(this.width, this.height);

		// move and update all asteroids
		updateAsteroids(brush);
	}
	
	/**
	* Private method used to update the asteroids currently in the game.
	* If an asteroid is off the screen this method handles removing it
	* If an asteroid is on the screen this methods handles moving and painting it.
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	private void updateAsteroids(Graphics brush) {
		ArrayList<Asteroid> toRemove = new ArrayList<>();

		
		for (Asteroid a : this.asteroids) {
			
			// move asteroids and update on screen
			brush.setColor(new Color(180, 160, 140));
			a.paint(brush);
			a.move();
			
			// check if every asteroid's point is on screen
			if (!asteroidOnScreen(a)) {
				
				// if not on screen, add to list of astoierds to be removed
				toRemove.add(a);
			}
		}

		// remove all asteroids off screen from this.asteroids ArrayList
		for (Asteroid removedAsteroid : toRemove) {
			this.asteroids.remove(removedAsteroid);
		}
	}

	/**
	* Method used to check if an asteroid is on the screen or not.
	* It does this by checking if the background contains the asteroid.
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	public boolean asteroidOnScreen(Asteroid a) {
		for (Point p : a.getPoints()) {
			if (this.background.contains(p)) {
				return true;
			}
		}
		return false;
	}

	
	/**
	* The main paint for AsteroidsGame.
	* This is a wrapper method that encapsulates more complex methods.
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	public void paint(Graphics brush) {

		if (GameData.gameOver == true) {
			endGame(brush);
			return;
		}
		
		// update game 
		updateGame(brush);
	}
	
	/**
	* This method is used to display the End Game screen.
	* Does not do anything apart from display some text
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	private void endGame(Graphics brush) {
		brush.setColor(Color.white);
		brush.setFont(new Font("Impact", Font.PLAIN, 30));
		brush.drawString("GAME OVER", this.width / 2 - 100, this.height / 2 - 100);
		brush.drawString("SCORE: " + GameData.points, this.width / 2 - 100, this.height / 2);
	}
	
	/**
	* The main method for AsteroidsGame
	* 
	* 
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	public static void main(String[] args) {
		AsteroidsGame a = new AsteroidsGame();
		a.repaint();
	}
}