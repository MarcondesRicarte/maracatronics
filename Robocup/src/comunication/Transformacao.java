package comunication;

public class Transformacao {

	public static void main(String[] args) {
		
		double norma = 1;
		double radius = 0;
		double pwmM1, pwmM2, pwmM3;
		for (int angle = 0; angle < 360; angle++) {
			radius = Math.toRadians(angle);
			pwmM1 = (byte) Math.round(100 * norma *
					Math.cos(radius + ((7 * Math.PI) / 6)));
			pwmM2 = (byte) Math.round(100 *
					Math.cos(radius + (Math.PI / 2)));
			pwmM3 = (byte) Math.round(100 *
					Math.cos(radius + (-Math.PI / 6)));
			System.out.println("angulo:" + angle);
			System.out.println("roda1:" + pwmM1);
			System.out.println("roda2:" + pwmM2);
			System.out.println("roda3:" + pwmM3);
			System.out.println();
		}
	}
	
	
}
