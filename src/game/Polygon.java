package game;

import java.awt.Graphics;
import java.util.Arrays;

/*
CLASS: Polygon
DESCRIPTION: A polygon is a sequence of points in space defined by a set of
             such points, an offset, and a rotation. The offset is the
             distance between the origin and the center of the shape.
             The rotation is measured in degrees, 0-360.
USAGE: You are intended to instantiate this class with a set of points that
       forever defines its shape, and then modify it by repositioning and
       rotating that shape. In defining the shape, the relative positions
       of the points you provide are used, in other words: {(0,1),(1,1),(1,0)}
       is the same shape as {(9,10),(10,10),(10,9)}.
NOTE: You don't need to worry about the "magic math" details.

 */

class Polygon {
	private Point[] shape;   // An array of points.
	public Point position;   // The offset mentioned above.
	public double rotation; // Zero degrees is due east.

	public Polygon(Point[] inShape, Point inPosition, double inRotation) {
		shape = inShape;
		position = inPosition;
		rotation = inRotation;

		// First, we find the shape's top-most left-most boundary, its origin.
		Point origin = shape[0].clone();
		for (Point p : shape) {
			if (p.x < origin.x) origin.x = p.x;
			if (p.y < origin.y) origin.y = p.y;
		}

		// Then, we orient all of its points relative to the real origin.
		for (Point p : shape) {
			p.x -= origin.x;
			p.y -= origin.y;
		}
	}

	// "getPoints" applies the rotation and offset to the shape of the polygon.
	public Point[] getPoints() {
		Point center = findCenter();
		Point[] points = new Point[shape.length];
		for (int i = 0; i < shape.length; i++) {
			//    for (Point p : shape) {
			Point p = shape[i];
			double x = ((p.x-center.x) * Math.cos(Math.toRadians(rotation)))
					- ((p.y-center.y) * Math.sin(Math.toRadians(rotation)))
					+ center.x + position.x;
			double y = ((p.x-center.x) * Math.sin(Math.toRadians(rotation)))
					+ ((p.y-center.y) * Math.cos(Math.toRadians(rotation)))
					+ center.y + position.y;
			//System.out.println("Center: " + center + "\tPoint: " + p + "\tRotation: " + rotation);
			points[i] = new Point(x,y);
		}
		return points;
	}

	// "contains" implements some magical math (i.e. the ray-casting algorithm).
	public boolean contains(Point point) {
		Point[] points = getPoints();
		double crossingNumber = 0;
		for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
			if ((((points[i].x < point.x) && (point.x <= points[j].x)) ||
					((points[j].x < point.x) && (point.x <= points[i].x))) &&
					(point.y > points[i].y + (points[j].y-points[i].y)/
							(points[j].x - points[i].x) * (point.x - points[i].x))) {
				crossingNumber++;
			}
		}
		return crossingNumber%2 == 1;
	}

	public void rotate(int degrees) {rotation = (rotation+degrees)%360;}

	/*
  The following methods are private access restricted because, as this access
  level always implies, they are intended for use only as helpers of the
  methods in this class that are not private. They can't be used anywhere else.
	 */

	// "findArea" implements some more magic math.
	private double findArea() {
		double sum = 0;
		for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
			sum += shape[i].x*shape[j].y-shape[j].x*shape[i].y;
		}
		return Math.abs(sum/2);
	}

	// "findCenter" implements another bit of math.
	private Point findCenter() {
		Point sum = new Point(0,0);
		for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
			sum.x += (shape[i].x + shape[j].x)
					* (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
			sum.y += (shape[i].y + shape[j].y)
					* (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
		}
		double area = findArea();
		return new Point(Math.abs(sum.x/(6*area)),Math.abs(sum.y/(6*area)));
	}

	/**
	* Default paint method for any shape that uses its points and the 
	* fillPolygon method from the Graphics class to update shpaes on screen.
	* 
	* @param brush
	* 
	*/
	protected void paint(Graphics brush) {
    	
    	// access Polygon's shape instance variable 
    	Point[] polygonPoints = this.getPoints();
    	
    	// loop through Polygon instance variable for points   	
    	int numPoints = polygonPoints.length;
    	int[] xPoints = new int[numPoints], yPoints = new int[numPoints];
    	
    	// create two arrays for x-coords and y-coords
    	for (int idx = 0; idx < numPoints; idx++) {
    		xPoints[idx] = (int) polygonPoints[idx].getX();
    		yPoints[idx] = (int) polygonPoints[idx].getY();
    	}
    	
    	// cdraw spaceship using x-coords and y-coords
    	brush.fillPolygon(xPoints, yPoints, numPoints);
    }
    
	/**
	* Default paint method for any shape that uses its poitns and the 
	* fillPolygon method from the Graphics class to update shpaes on screen.
	* 
	* @param other another Polygon object to check for collision
	* @return boolean that represents whether the two polygons are colliding
	*/
    protected boolean collides(Polygon other) {
    	
    	// loops through all points of the other polygon
    	for (Point point: other.getPoints()) {
    		
    		// if a point in the other polygon are contained within "this"
    		// then the shapes are touching
    			if (this.contains(point)) {
    				return true;
    			}
    	}
    	
    	// otherwise they are not touching
    	return false;
    }
    
    /**
     * Sends an object to the other side of the screen if all its points pass
     * a given edge (top, bottom, left or right).
     * 
     * @param width width of the screen
     * @param height height of the screen
     * 
     */
	protected void wrapScreen(int width, int height) {
		
		// find current position
		Point position = this.position;
		double currX = position.getX();
		double currY = position.getY();

		// check all directions of screen and updates position to oppsite side 
		// of screen
		if (currX <= 0) {
			position.setX(currX + width);
		} else if (currX > width) {
			position.setX(currX - width);
		} else if (currY <= 0) {
			position.setY(currY + height);
		} else if (currY > height) {
			position.setY(currY - height);
		}
	}
}