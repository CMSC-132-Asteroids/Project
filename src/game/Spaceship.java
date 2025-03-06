package game;

public class Spaceship extends Polygon implements Collidable {
	
	/*
	1. constructor to create spaceship
	- triangle (for now)
	- position = center of screen
	- rotation = 0.0 (or rotate 90 degrees to point in certain direction)
	2. implement collidable methods
	- use Polygon's .contains() method to check if its points lies within another
	collidable object
	- (other collidable object will also check if they're points are within THIS
	object)
	- loss a life method on spaceship collision
	3. shoot a laser
	- create nested class
	- method creates instance of laser and adds arraylist of lasers
	- lasers will also check for collidable with asteroids/boss
	- lasers start with orientation of spaceship and move toward that direction
	4. rotation (and possibly movement later)
	- method to take in input and rotate spaceship accordingly (does input rotation need to != 0)?
	
	 */
	
	int speed = 0;
	
	private class Laser extends Polygon {
		int laserSpeed = 0;
		
		public Laser(Point[] points, Point offset, double rotation, Point p) {
			super(points, offset, rotation);
		}
	}
 	
	// may add more parameters
    public Spaceship(Point[] points, Point offset, double rotation) {
        super (points, offset, rotation);
    }

	@Override
	public boolean isCollision(Collidable other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void collide() {
		// TODO Auto-generated method stub
		
	}
    
}
