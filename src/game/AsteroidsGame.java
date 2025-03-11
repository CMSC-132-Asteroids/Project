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
import game.AsteroidsGame.PointHolder;

class AsteroidsGame extends Game {
	static int startTimer = 100;
	static int asteroidTimer = 0;
	static double asteroidSpawnFactor = 1;
	static boolean goldenAsteroid = false;
	
	
	//This is so we can pass the points by reference
	public static class PointHolder {
		public static int points = 0;
	}

	/*
	instance variables to keep track of objects?
	- spaceship
	- arraylist of asteroids (may contain golden asteroid)
	- current boss
	 */
	private Spaceship spaceship;
	private Boss boss;
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
		
		//Creates the boss but does not display the boss
		initializeBoss();

	}

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
	
	public void initializeBoss() {
		int length = 30;
		int height = (int) (Math.sqrt(3) / 2 * length);
		Point[] bossPoints = {new Point(0, 0), new Point(length / 2, 
				height / 5), new Point(length, 0), new Point(length / 2, height)};

		// calculate offset to place center of spaceship at center of screen
		int xCenter = this.width / 2 - length / 2, yCenter = this.height / 5 - 
				height / 2 ; 

		Point position = new Point(xCenter, yCenter); 
		
		this.boss = new Boss(bossPoints, position, 0);
		this.boss.setDisplay();
	}

	public void spawnAsteroid() {

		// check CD for asteroid
		if (asteroidTimer == 0) {
			
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
			if (goldenAsteroid == false && PointHolder.points >= 0) {

				goldenAsteroid = true;
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
						PointHolder.points += 1000;

					}
				});
			} else {
				this.asteroids.add(new Asteroid(asteroidPoints, spawnPoint, rotation));
			}

			
			// add random CD to timer
			asteroidTimer += asteroidSpawnFactor * random.nextInt(50, 100);
			if (asteroidSpawnFactor > 0.1) {
				asteroidSpawnFactor *= .995;
			}
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
		
		// update points
		brush.setColor(Color.white);
		brush.setFont(new Font("Impact", Font.PLAIN, 30));
		brush.drawString("Points: " + PointHolder.points, 20, this.height - 90);
		
		brush.setColor(Color.red);
		brush.setFont(new Font("Helvetica", Font.PLAIN, 30));
		
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
				brush.drawString(":(", 20, this.height/20);
				break;
		}

		// move and update spaceship (includes lasers)
		brush.setColor(Color.white);
		this.spaceship.move();
		this.spaceship.paint(brush, this.asteroids, this.background);
		
		//If the boss exists and is displayable do this
		if(this.boss != null && this.boss.getDisplay()) {
			this.boss.move();
			this.boss.paint(brush);
			this.boss.wrapScreen(this.width, this.height);
		}

		// adjustElementPositions();
		spaceship.wrapScreen(this.width, this.height);

		// move and update all asteroids
		updateAsteroids(brush);
	}

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

	public boolean asteroidOnScreen(Asteroid a) {
		for (Point p : a.getPoints()) {
			if (this.background.contains(p)) {
				return true;
			}
		}
		return false;
	}

	public void paint(Graphics brush) {



		// update game 
		updateGame(brush);
	}

	public static void main(String[] args) {
		AsteroidsGame a = new AsteroidsGame();
		a.repaint();
	}
}