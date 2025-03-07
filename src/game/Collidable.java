package game;

public interface Collidable {

	
	
	/*
	1. isCollision(Collidable other) 
	- uses Polygon's contains() methods to check if points of current object contained
	within points of another object
		- each object always constantly checking if its touching other collidable objects 
		(stored in arraylist?) > some objects don't need to check 
			- lasers should never hit each other
			- space ship should never hit a laser
	
	2. method that checks what to do on collision? > overriden separately in each implmementing class
	- asteroid and laser get destroyed on contact
	- boss loses 1hp and laser destroyed on contact
	- player loses life and asteroid destroyed on contact
	- ALL POINTS OF OBJECT OFF SCREEN? 
		> destroy object (e.g. lasers, asteroids, BUT NOT boss/spaceship)
		> if time boss and player should wrap around screen
		> create variable that becomes true once the center of all element's are on screen
			after that, if onScreen = true && (Point p : this.getPoints) !points on screen?, then
	
	 */
	
	public boolean collides(Collidable other);
	
}
