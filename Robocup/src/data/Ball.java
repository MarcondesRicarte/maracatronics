package data;

/**
 * 
 * @author Marcondes Ricarte
 *
 */

public class Ball {

	// public static double CHARGE = -1;
	
	private double x;
	private double y;
	private double confidence;
	
	public Ball(){
		this.x = 0;
		this.y = 0;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	
}
