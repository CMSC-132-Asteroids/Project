package game;

/**
* Damagable interface represents objects that can take damage.
* 
* @author Declan Amoako
* @author Allen Bai
* 
*/
public interface Damagable {
	/**
	* Gets the current health of an object.
	* 
	* @return int representing the current health
	* 
	*/
	int getHealth();
	
	/**
	* Reduces the current health of an object
	* 
	*/
	void getHit();
}
