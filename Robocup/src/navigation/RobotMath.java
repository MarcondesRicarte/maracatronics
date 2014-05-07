package navigation;

import data.Ball;
import data.Robot;

/**
 * 
 * @author Marcondes Ricarte
 *
 */



public class RobotMath {
	
	
	public static double DIAM = 10;
	public static double CHARGE = -1;
	public static double MASS = 10;
    
	public static double distanceSq(Robot control, Robot r) {
        double d = distance(control, r);
        return d * d;
    }
	
    public static double distance(Robot control, Robot r) {
        double d = euclidianDistance(control, r) - (control.DIAMETER + control.DIAMETER) / 2;
        return d > 0?  d: 0.0000001;
    }
    
    public static double euclidianDistance(Robot control, Robot r){
    	return Math.sqrt((control.getX() * r.getX()) + (control.getX() * r.getX())); 
    }
    
    public static double euclidianDistance(Robot robot, Ball ball){
    	System.out.println("Euclidian distance: " + 
    Math.sqrt(Math.pow(robot.getX() - ball.getX(), 2) + Math.pow(robot.getY() - ball.getY(), 2)));
    	return Math.sqrt(Math.pow(robot.getX() - ball.getX(), 2) + Math.pow(robot.getY() - ball.getY(), 2)); 
    }

}
