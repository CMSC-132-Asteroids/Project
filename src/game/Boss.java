package game;

public class Boss extends Polygon {

	// default constructor > may add more attributes later
	/*
	level? > determines HP/speed/attributes of boss
	
	(if time at end? > random object > enum class to hold different boss shapes 
	that correlate to different numbers) 
	 */
    public Boss(Point[] points, Point offset, double rotation) {
        super (points, offset, rotation);
    }
}
