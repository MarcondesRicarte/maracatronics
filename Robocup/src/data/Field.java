package data;


/**
 * 
 * @author Marcondes Ricarte
 *
 */

public class Field {
	
	private static Field field;
	
	public final int ROBOTS = 6;
	private Ball ball;
	public Robot me [];
	public Robot oppenent [];
	
	
	private Field(){
		this.ball = new Ball();
		this.me = new Robot[this.ROBOTS];
		this.oppenent = new Robot[this.ROBOTS];
		this.inicialize();
	}
	
	public static synchronized Field getInstance(){
		if(field == null){
			field = new Field();
		}
		return field;
	}
	
	public void inicialize(){
		for (int i = 0; i < this.ROBOTS; i++) {
			this.me[i] = new Robot();
			this.me[i] = new Robot();
		}
	}


	public Ball getBall() {
		return ball;
	}


	public void setBall(Ball ball) {
		this.ball = ball;
	}


	public Robot[] getMe() {
		return me;
	}


	public void setMe(Robot[] me) {
		this.me = me;
	}
	
	
	public Robot getRobotMe(int index) {
		return this.me[index];
	}
	
	public Robot getRobotOpponent(int index) {
		return this.oppenent[index];
	}


	public Robot[] getOppenent() {
		return oppenent;
	}


	public void setOppenent(Robot[] oppenent) {
		this.oppenent = oppenent;
	}

}
