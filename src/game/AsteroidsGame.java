package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import game.Point;


/**
 * AsteroidsGame is the class that holds the main game event loop.
 * It holds all the objects in the game, and decides their behavior.
 * It extends the Abstract class which provides us all the functionality 
 * needed to display a window.
 * 
 * @author Declan Amoako
 * @author Allen Bai
 * 
 */
class AsteroidsGame extends Game {

	/**
	 * The GameData inner class is designed to hold all the data needed the game 
	 * such as current score, whether the game is over, a cooldown for the
	 * next asteroid spawn and the points necessary for the boss to spawn.
	 * 
	 * @author Declan Amoako
	 * @author Allen Bai
	 * 
	 */
	public static class GameData {
		protected static int points = 0;
		private static int startTimer = 100;
		private static int asteroidTimer = 0;
		private static double asteroidSpawnFactor = 1;
		private static boolean goldenAsteroid = false;
		private static boolean gameOver = false;
		private static int bossThreshold = 500;
	}

	private Spaceship spaceship;
	private Boss boss;
	private Polygon background;
	private ArrayList<Asteroid> asteroids = new ArrayList<>();

	/**
	 * Default constructor for AsteroidGame
	 * It initializes all the main game objects including spaceship, boss
	 * and background.
	 * 
	 */
	public AsteroidsGame() {
		super("Asteroids!", 0, 0);
		this.setFocusable(true);
		this.requestFocus();

		// initialize background
		initializeBackground();

		// create spaceship
		initializeSpaceship();

		// creates but does not display boss
		initializeBoss();

	}

	/**
	 * Initializes the background using the size of the screen.
	 * The background is used to determine when an object is off screen.
	 * 
	 */
	private void initializeBackground() {

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
	 */
	private void initializeSpaceship() {

		// create equalateral triangle (with divet)
		int length = 30;
		int height = (int) (Math.sqrt(3) / 2 * length);
		Point[] spaceshipPoints = {new Point(0, 0), new Point(length / 2, 
				height / 5), new Point(length, 0), new Point(length / 2, 
						height)};

		// calculate offset to place center of spaceship at center of screen
		int xCenter = this.width / 2 - length / 2, yCenter = this.height / 2 - 
				height / 2 ;
		Point position = new Point(xCenter, yCenter); 
		double rotation = 180.0;
		this.spaceship = new Spaceship(spaceshipPoints, position, rotation);

		// give spaceship a way to track keystrokes
		this.addKeyListener(spaceship);
	}

	/**
	 * Initializes the Boss.
	 * The Boss is the hardest objective for the game that only occurs appears 
	 * once the player has a certain amount of points (starting at 500 and 
	 * doubling each time the boss is defeated).
	 * 
	 */
	private void initializeBoss() {
		int length = 50;
		int height = (int) (Math.sqrt(3) / 2 * length);
		Point[] bossPoints = {new Point(0, 0), new Point(200, 0),  
				new Point(200, 200), new Point(0, 200)};

		// calculate offset to place center of boss at center of screen
		int xCenter = this.width / 2 - length / 2, yCenter = this.height / 5 - 
				height / 2 ; 
		Point position = new Point(xCenter, yCenter); 		
		this.boss = new Boss(bossPoints, position, 0);
	}

	/**
	 * Spawns an asteroid on a random edge of the screen if an the cooldown for
	 * an asteroid spawn is 0. The trajectory is set toward the player's current
	 * position and the shape is determined randomly. A golden asteroid will also
	 * be spawned when the player has at least 1000 points.
	 * 
	 */
	private void spawnAsteroid() {

		// check CD for asteroid
		if (GameData.asteroidTimer == 0) {

			// randomize number of points, size and spawn location
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
			spawnX = random.nextInt(-scale, this.width + scale);
			spawnY = (spawnLocation == 0) ? -scale : this.height - scale * 2;
			rotation = Math.toDegrees(Math.acos(Math.abs((spawnX - spaceshipX)) 
					/ (Math.sqrt(Math.pow(Math.abs(spawnX - spaceshipX), 2) + 
							Math.pow(spaceshipY, 2)))));

			// adjust asteroid rotation based on spawn position
			AsteroidRotator topRight = ((angle) -> (-angle));
			AsteroidRotator topLeft = ((angle) -> (180 + angle));
			AsteroidRotator bottomRight = ((angle) -> (angle));
			AsteroidRotator bottomLeft = ((angle) -> (180 - angle));
			if (spawnX > spaceshipX) {
				rotation = (spawnY < 0) ? topRight.rotate(rotation) : 
					bottomRight.rotate(rotation);
			} else {
				rotation = (spawnY < 0) ? topLeft.rotate(rotation) : 
					bottomLeft.rotate(rotation);
			}

			// randomly adjust rotation so asteroid's don't fire directly at 
			// player
			rotation += random.nextInt(-20, 20);

			// create the spawn point of the asteroid to be used as its offset
			Point spawnPoint = new Point(spawnX, spawnY);

			// generate points of asteroid
			for (int point = 0; point < numPoints; point++) {
				int currAngle = point * 360 / numPoints;
				double x = scale * Math.cos(Math.toRadians(currAngle));
				double y = scale * Math.sin(Math.toRadians(currAngle));
				asteroidPoints[point] = new Point(x, y);
			}

			// check if a golden asteroid can be spawned
			if (GameData.goldenAsteroid == false && GameData.points >= 1000) {

				// golden asteroids can only be spawned once
				GameData.goldenAsteroid = true;

				// randomize numbe of sides and set its scale
				numPoints = random.nextInt(5, 12) * 2;
				scale = 20;
				asteroidPoints = new Point[numPoints];

				// generate asteroid points
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

				// add asteroid to list of all asteroids
				this.asteroids.add(new Asteroid(asteroidPoints, spawnPoint, 
						rotation) {		

					@Override
					public void move() {

						// calculate change in asteroid's coordinates using 
						// rotation and velocity
						double changeX = baseVelocity * 2 * 
								Math.cos(Math.toRadians(this.rotation));
						double changeY = baseVelocity * 2 * 
								Math.sin(Math.toRadians(this.rotation));

						// update asteroid's position
						double currX = this.position.getX(), currY = 
								this.position.getY();
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
						int[] xPoints = new int[numPoints], yPoints = new 
								int[numPoints];

						// create two arrays for x-coords and y-coords
						for (int idx = 0; idx < numPoints; idx++) {
							xPoints[idx] = (int) spaceshipPoints[idx].getX();
							yPoints[idx] = (int) spaceshipPoints[idx].getY();
						}

						// draw asteroid using x-coords and y-coords
						brush.fillPolygon(xPoints, yPoints, numPoints);
						brush.setColor(new Color(180, 160, 140));
					}

					@Override
					public void changePoints(Boss boss) {
						GameData.points += 1000;
					}
				});

				// spawn a normal asteroid if golden asteroid not spawned
			} else {this.asteroids.add(new Asteroid(asteroidPoints, spawnPoint, 
					rotation));}

			// reset cooldown of asteroid spawn timer and decrease it by the
			// current spawn factor
			GameData.asteroidTimer += GameData.asteroidSpawnFactor * 
					random.nextInt(50, 100);

			// decrease asteroid spawn factor each time an asteroid is spawned
			if (GameData.asteroidSpawnFactor > 0.1) {
				GameData.asteroidSpawnFactor *= .995;
			}
		}

		// decrement the cooldown to spawn next asteroid
		GameData.asteroidTimer--;
	}


	/**
	 * This is the main game loop.
	 * The main game loop updates the spaceship (and lasers), asteroids, 
	 * background and boss.
	 * It calls the move and paint methods of the game objects.
	 * 
	 * @param brush
	 * 
	 */
	private void updateGame(Graphics brush) {

		// decrement time until first asteroid spawn
		if (GameData.startTimer > 0) {
			GameData.startTimer--;
		} else {
			spawnAsteroid();
		}

		// update background
		brush.setColor(Color.black);
		this.background.paint(brush);

		// update player's score
		brush.setColor(Color.white);
		brush.setFont(new Font("Impact", Font.PLAIN, 30));
		brush.drawString("Points: " + GameData.points, 20, this.height - 90);

		// update player's lives
		brush.setColor(Color.red);
		brush.setFont(new Font("Helvetica", Font.PLAIN, 30));
		updatePlayerLives(brush);

		// move and update spaceship (including lasers)
		brush.setColor(Color.white);
		this.spaceship.takeInput();
		this.spaceship.paint(brush, this.asteroids, this.background, this.boss);
		spaceship.wrapScreen(this.width, this.height);

		// check if player has enough points to spawn next boss
		if (GameData.points >= GameData.bossThreshold && 
				!this.boss.getVisibility()) {
			this.boss.changeVisibility();
			GameData.bossThreshold *= 2;
		}

		// move and update boss' lives and position on screen 
		updateBossLives(brush);
		this.boss.move();
		this.boss.paint(brush);
		this.boss.wrapScreen(this.width, this.height);

		// move and update all asteroids
		updateAsteroids(brush);
	}

	/**
	 * Updates the hearts of the player displayed on screen, checking for
	 * collisions with the boss.
	 * 
	 * @param brush
	 * 
	 */
	private void updatePlayerLives(Graphics brush) {
		if (this.spaceship.collides(this.boss) && !this.spaceship.getInv() && 
				this.boss.getVisibility()) {
			this.spaceship.getHit();
			this.spaceship.setInv(true);
		}
		switch (this.spaceship.getHealth()) {
		case 3:
			brush.drawString("❤️❤️❤️", 20, this.height / 20);
			break;
		case 2:
			brush.drawString("❤️❤️", 20, this.height / 20);
			break;
		case 1:
			brush.drawString("❤️", 20, this.height / 20);
			break;
		default:
			brush.drawString("", 20, this.height / 20);
			GameData.gameOver = true;
			break;
		}
	}

	/**
	 * Updates the hearts of the boss displayed above boss, checking if the boss
	 * is currently on screen.
	 * 
	 * @param brush
	 * 
	 */
	private void updateBossLives(Graphics brush) {

		// display current boss's lives when on screen
		if (this.boss != null && this.boss.getVisibility()) {
			brush.setColor(Color.yellow);
			brush.setFont(new Font("Helvetica", Font.PLAIN, 30));
			Point position = this.boss.position;
			int x = (int) position.getX() + 50;
			int y = (int) position.getY() - 10;
			switch (this.boss.getHealth()) {
			case 5:
				brush.drawString("❤️❤️❤️❤️❤️", x, y);
				break;
			case 4:
				brush.drawString("❤️❤️❤️❤️", x, y);
				break;
			case 3:
				brush.drawString("❤️❤️❤️", x, y);
				break;
			case 2:
				brush.drawString("❤️❤️", x, y);
				break;
			case 1:
				brush.drawString("❤️", x, y);
				break;
			default:
				brush.drawString("", x, y);
				this.spaceship.setHealth(3);
				this.boss.changeVisibility();
				break;
			}
		}
	}

	/**
	 * Updates all asteroids, moving and painting their new positions on screen.
	 * Asteroids off screen will be removed.
	 * 
	 * @param brush
	 */
	private void updateAsteroids(Graphics brush) {

		// create new list of asteroids that are offscreen
		ArrayList<Asteroid> toRemove = new ArrayList<>();

		// loop through all current asteroids
		for (Asteroid a : this.asteroids) {

			// move and paint each asteroid 
			brush.setColor(new Color(180, 160, 140));
			a.paint(brush);
			a.move();

			// check if every asteroid's point is on screen
			if (!this.background.collides(a)) {

				// if not on screen, add to list of astoierds to be removed
				toRemove.add(a);
			}
		}

		// remove all asteroids off screen from game's list of asteroids
		for (Asteroid removedAsteroid : toRemove) {
			this.asteroids.remove(removedAsteroid);
		}
	}

	/**
	 * Updates the screen by calling updateGame unless the game is over.
	 * 
	 * @param brush
	 * 
	 */
	public void paint(Graphics brush) {
		
		// check if gamve over
		if (GameData.gameOver == true) {
			endGame(brush);
			return;
		}

		// update game 
		updateGame(brush);
	}

	/**
	 * Displays a game over screen with the player's total score if the player
	 * has no more lives.
	 * 
	 * @param brush
	 * 
	 */
	private void endGame(Graphics brush) {
		brush.setColor(Color.white);
		brush.setFont(new Font("Impact", Font.PLAIN, 30));
		brush.drawString("GAME OVER", this.width / 2 - 100, this.height / 2 - 100);
		brush.drawString("SCORE: " + GameData.points, this.width / 2 - 100, this.height / 2);
	}

	/**
	 * Main method to get game running.
	 * 
	 */
	public static void main(String[] args) {
		AsteroidsGame a = new AsteroidsGame();
		a.repaint();
	}
}