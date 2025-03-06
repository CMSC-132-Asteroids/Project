package game;

public class Asteroid extends Polygon {
	
	/*
	1. generate instance variables
	- generate shape method (start off with circle > may generate random points) 
	- generate original position/offset (at edge of screen > offscreen if time?)
	- rotation should face toward center of screen and move toward player
	- (two rotation variable to keep real rotation and another to rotate graphically while it moves?)
	2. collidable interface methods
	- collides with boss
	- collides with lasers
	- collides with other asteroids
	- method for asteroid to destroy itself on contact?
	3. method for asteroid to move (and possibly spin)
	 */
	
	
    public Asteroid(Point[] points, Point offset, double rotation) {
        super (points, offset, rotation);
    }    
}
