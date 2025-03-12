package game;



@FunctionalInterface
public interface RotationCalculator {
	
	// take in original angle and convert it based on spawn position
	public double getRotation(double angle);
}
