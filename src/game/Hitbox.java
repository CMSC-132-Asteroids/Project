package game;

public class Hitbox {
	Polygon shape;
	
	public Hitbox(Point[] points, Point offset, double rotation) {
		this.shape = new Polygon(points, offset, rotation);
	}
	
	public void bounds() {
		
	}
}
