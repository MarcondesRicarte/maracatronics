package data;

/**
 * 
 * @author Marcondes Ricarte
 *
 */

public class Robot {
	
	public static final double DIAMETER = 1.7; 
	public static final double CHARGE = -1;
	
	private int id;
	private double x;
	private double y;
	private double angle;
	private double confidence;
	
	private String ip;
	private byte[] buffer2;
	private float timeStam;
	private float wheel1; 
	private float wheel2; 
	private float wheel3;
	private float wheel4;
	private float kickspeedx;
	private float kickspeedz;
	private float velx;
	private float vely;
	private float velz;
	
	private boolean spinner; 
	private boolean wheelSpeed;
	private boolean teamYellow;

	
	public Robot(){
		this.x = 0;
		this.y = 0;
		this.angle = 0;
	}
	
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
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

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}



	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}



	public byte[] getBuffer2() {
		return buffer2;
	}



	public void setBuffer2(byte[] buffer2) {
		this.buffer2 = buffer2;
	}



	public float getTimeStam() {
		return timeStam;
	}



	public void setTimeStam(float timeStam) {
		this.timeStam = timeStam;
	}



	public float getWheel1() {
		return wheel1;
	}



	public void setWheel1(float wheel1) {
		this.wheel1 = wheel1;
	}



	public float getWheel2() {
		return wheel2;
	}



	public void setWheel2(float wheel2) {
		this.wheel2 = wheel2;
	}



	public float getWheel3() {
		return wheel3;
	}



	public void setWheel3(float wheel3) {
		this.wheel3 = wheel3;
	}



	public float getWheel4() {
		return wheel4;
	}



	public void setWheel4(float wheel4) {
		this.wheel4 = wheel4;
	}



	public float getKickspeedx() {
		return kickspeedx;
	}



	public void setKickspeedx(float kickspeedx) {
		this.kickspeedx = kickspeedx;
	}



	public float getKickspeedz() {
		return kickspeedz;
	}



	public void setKickspeedz(float kickspeedz) {
		this.kickspeedz = kickspeedz;
	}



	public float getVelx() {
		return velx;
	}



	public void setVelx(float velx) {
		this.velx = velx;
	}



	public float getVely() {
		return vely;
	}



	public void setVely(float vely) {
		this.vely = vely;
	}



	public float getVelz() {
		return velz;
	}



	public void setVelz(float velz) {
		this.velz = velz;
	}



	public boolean isSpinner() {
		return spinner;
	}



	public void setSpinner(boolean spinner) {
		this.spinner = spinner;
	}



	public boolean isWheelSpeed() {
		return wheelSpeed;
	}



	public void setWheelSpeed(boolean wheelSpeed) {
		this.wheelSpeed = wheelSpeed;
	}



	public boolean isTeamYellow() {
		return teamYellow;
	}



	public void setTeamYellow(boolean teamYellow) {
		this.teamYellow = teamYellow;
	}
	
	
	 

}
