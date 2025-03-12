package game;

/**
* Damagable interface represents an object that can take damage
* 
* 
* 
* 
* @author Declan Amoako
* @author Allen Bai
* 
*/
public interface Damagable {
	/**
	* Gets the current health of an object
	* 
	* 
	* 
	* @return an integer representing the current health
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	int getHealth();
	
	/**
	* Gets the current health of an object
	* 
	* 
	* 
	* @param takes an integer that represents the amount to take from the objects health
	* @author Declan Amoako
	* @author Allen Bai
	* 
	*/
	void takeHealth(int amt);
}
