package comunication;

import robocup.messages.MessagesRobocupSslWrapper.SSL_WrapperPacket;
import data.Field;

/**
 * 
 * @author Marcondes Ricarte
 *
 */

public class ParserReader {

	public static void parse(SSL_WrapperPacket parsEFrom, Field field){
		
        if(parsEFrom.getDetection().getBallsCount() != 0 && parsEFrom.getDetection().getBalls(0).getPixelX() != 0.0){
       		field.getBall().setX(parsEFrom.getDetection().getBalls(0).getPixelX());
       		field.getBall().setY(parsEFrom.getDetection().getBalls(0).getPixelY());
        }
        
        if(parsEFrom.getDetection().getRobotsBlueCount() != 0){
        	for (int i = 0; i < parsEFrom.getDetection().getRobotsBlueCount(); i++) {
        		if(field.getRobotMe(i) != null && parsEFrom.getDetection().getRobotsBlue(i).getPixelX() != 0.0){
	        		field.getRobotMe(i).setId(parsEFrom.getDetection().getRobotsBlue(i).getRobotId());
	        		field.getRobotMe(i).setX(parsEFrom.getDetection().getRobotsBlue(i).getPixelX());
	        		field.getRobotMe(i).setY(parsEFrom.getDetection().getRobotsBlue(i).getPixelY());
	        		field.getRobotMe(i).setAngle(parsEFrom.getDetection().getRobotsBlue(i).getOrientation());
	        		field.getRobotMe(i).setConfidence(parsEFrom.getDetection().getRobotsBlue(i).getConfidence());
	        	}
        	}
        }       
        
        if(parsEFrom.getDetection().getRobotsYellowCount() != 0){
        	for (int i = 0; i < parsEFrom.getDetection().getRobotsYellowCount(); i++) {
        		if(field.getRobotOpponent(i) != null && parsEFrom.getDetection().getRobotsYellow(i).getPixelX() != 0.0){
        			field.getRobotOpponent(i).setId(parsEFrom.getDetection().getRobotsYellow(i).getRobotId());
        			field.getRobotOpponent(i).setX(parsEFrom.getDetection().getRobotsYellow(i).getPixelX());
        			field.getRobotOpponent(i).setY(parsEFrom.getDetection().getRobotsYellow(i).getPixelY());
        			field.getRobotOpponent(i).setAngle(parsEFrom.getDetection().getRobotsYellow(i).getOrientation());
        			field.getRobotOpponent(i).setConfidence(parsEFrom.getDetection().getRobotsYellow(i).getConfidence());
        		}
        	}
        }
        
        //print 
        
        if(parsEFrom.getDetection().getBallsCount() != 0 && parsEFrom.getDetection().getBalls(0).getPixelX() != 0.0){
        	System.out.println("Ball: ");
        	System.out.println(parsEFrom.getDetection().getBalls(0).getPixelX());
       		System.out.println(parsEFrom.getDetection().getBalls(0).getPixelY());
        }
	}
}
