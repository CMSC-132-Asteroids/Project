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

class AsteroidsGame extends Game {
	static int points = 0;
	static int startTimer = 100;
	static int asteroidTimer = 0;
	static double asteroidSpawnFactor = 1;

	/*
	instance variables to keep track of objects?
	- spaceship
	- arraylist of asteroids (may contain golden asteroid)
	- current boss
	 */
	private Spaceship spaceship;
	private Polygon background; // may not be necessary...?
	private ArrayList<Asteroid> asteroids = new ArrayList<>();

	public AsteroidsGame() {
		super("Asteroids!", 0, 0);
		this.setFocusable(true);
		this.requestFocus();

		// initialize background
		initializeBackground();

		// create spaceship, track keystrokes for movement
		initializeSpaceship();

	}

	/*
	public void adjustElementPositions() {
		PositionChecker positionChecker = (Polygon obj) -> {
			Point[] points = obj.getPoints();

			// loop through all of object's points
			for (Point p : points) {

				// if any point within backgroudn, then it is on screen
				if (background.contains(p)) {
					return false;
				}
			}

			// if none of points within background, then it is off screen

			return true;
		};

		positionChecker.isOffScreen(this.spaceship);
	}
	 */

	public void initializeBackground() {

		// create rectangle background
		Point[] backgroundPoints = {new Point(0, 0), new Point(this.width, 0), 
				new Point(this.width, this.height), new Point(0, this.height)};
		Point offset = new Point(0, 0);
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

	public void spawnAsteroid() {

		// check CD for asteroid
		if (asteroidTimer == 0) {
			
			// randomize 
			Random random = new Random();
			int numPoints = random.nextInt(3,7);
			int scale = random.nextInt(25, 75); //random.nextDouble(1, 3);
			Point[] asteroidPoints = new Point[numPoints];
			int spawnLocation = random.nextInt(0, 2);
			int spawnX, spawnY; 
			double rotation;
			
			// get spaceship's position
			Point spaceshipPos = this.spaceship.position;
			double spaceshipX = spaceshipPos.getX();
			double spaceshipY = spaceshipPos.getY();

			// calculate angle of asteroid based on spaceship and spawn position
			switch (spawnLocation) {
			case 0: 
				spawnX = random.nextInt(-scale, this.width + scale);
				spawnY = -scale;
				if (spawnX > spaceshipX) {
					rotation = - Math.toDegrees(Math.acos((spawnX - spaceshipX) / 
							(Math.sqrt(Math.pow(spawnX - spaceshipX, 2) + 
									Math.pow(spaceshipY, 2)))));
				} else {
					rotation = 180 + Math.toDegrees(Math.acos((spaceshipX - spawnX) / 
							(Math.sqrt(Math.pow(spaceshipX - spawnX, 2) + 
									Math.pow(spaceshipY, 2)))));
				}
				break;
			default:
				spawnX = random.nextInt(-scale, this.width + scale);
				spawnY = this.height - scale * 2;
				if (spawnX > spaceshipX) {
					rotation = Math.toDegrees(Math.acos((spawnX - spaceshipX) / 
							(Math.sqrt(Math.pow(spawnX - spaceshipX, 2) + 
									Math.pow(spaceshipY, 2)))));
				} else {
					rotation = 180 - Math.toDegrees(Math.acos((spaceshipX - spawnX) / 
							(Math.sqrt(Math.pow(spaceshipX - spawnX, 2) + 
									Math.pow(spaceshipY, 2)))));
				}
				break;
			}
			
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
			this.asteroids.add(new Asteroid(asteroidPoints, spawnPoint, rotation));
			
			// add random CD to timer
			asteroidTimer += asteroidSpawnFactor * random.nextInt(50, 100);
			if (asteroidSpawnFactor > 0.1) {
				asteroidSpawnFactor *= .99;
			}
			System.out.println(asteroidSpawnFactor);
		}
		asteroidTimer--;
	}
	
	public void updateGame(Graphics brush) {

		// decrement time until first asteroid spawn
		if (startTimer > 0) {
			startTimer--;
		} else {
			spawnAsteroid();
		}

		// update background
		brush.setColor(Color.black);
		this.background.paint(brush);

		// move and update spaceship (includes lasers)
		brush.setColor(Color.white);
		this.spaceship.move();
		this.spaceship.paint(brush);

		// adjustElementPositions();
		spaceship.wrapScreen(this.width, this.height);

		// move and update all asteroids
		brush.setColor(new Color(140, 120, 100));
		updateAsteroids(brush);
	}

	private void updateAsteroids(Graphics brush) {
		ArrayList<Asteroid> toRemove = new ArrayList<>();

		for (Asteroid a : this.asteroids) {
			
			// move asteroids and update on screen
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

	public boolean asteroidOnScreen(Asteroid a) {
		for (Point p : a.getPoints()) {
			if (this.background.contains(p)) {
				return true;
			}
		}
		return false;
	}

	public void paint(Graphics brush) {

		// update points
		brush.setColor(Color.white);
		brush.drawString("Points: " + points, 20, 20);

		// update game 
		updateGame(brush);
	}

	public static void main(String[] args) {
		AsteroidsGame a = new AsteroidsGame();
		a.repaint();
	}
}