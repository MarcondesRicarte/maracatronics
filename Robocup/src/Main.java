import navigation.RobotController;

import comunication.CameraReader;

import data.Field;

/**
 * 
 * @author Marcondes Ricarte
 *
 */

public class Main {

	public static void main(String[] args) {	
		 CameraReader cameraReader = new CameraReader();
		 Field field = Field.getInstance();
		 RobotController robotController = new RobotController();
		 while(true){
			 cameraReader.read();
			 robotController.goToBall(cameraReader, field, field.getRobotMe(1), field.getBall());
			 //r = new RobotAlg(field.me[0], field.getOppenent(), field.getBall(),  0.1, 40, 4000, 15);
	   	 }
	}
	
}
