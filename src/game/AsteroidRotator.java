package game;

/**
* The AsteroidRotator interface allows for the instantiation of objects that
* takes an asteroid's rotation and adjusts it to face toward the spaceship 
* depending on which quadrant the asteroid spawned in.
* 
* @author Declan Amoako
* @author Allen Bai
* 
*/
@FunctionalInterface
public interface AsteroidRotator {
	
	/**
	* Calculates the new rotation of an asteroid.
	* 
	* @param takes in the current rotation of the asteroid
	* @return double representing the angle facing toward the spaceship
	* 
	*/
	double rotate(double angle);
}
