package comunication;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import jssc.SerialPort;
import jssc.SerialPortException;

public class ComunicationDefault {

	
	public static void main(String[] args) {
    	SerialPort serialPort = new SerialPort("\\\\\\\\.\\\\COM4");
    	//0 = horario
        //SerialPort serialPort = new SerialPort("\\\\\\\\.\\\\COM24"); //mudar porta
//        boolean flag = false;
    	byte times = 0;
        int cont  = 0;
        int erro = 0;
        int a = 0;
        byte id;
        byte pwmM1;
        byte pwmM2;
        byte pwmM3;
        byte dirM1;
        byte dirM2;
        byte dirM3;
        byte chute;
        byte drible;
        byte bateria;
        Scanner in = new Scanner(System.in);
        try {
            serialPort.openPort();//Open serial port
                               serialPort.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            //Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
            while(times <= 50){ //qtd De vezes
            	
                System.out.println("ID:");
                
                id = in.nextByte();
                if(id == 2){
                	pwmM1 = 0;
                	dirM1 = 0;
                	pwmM2 = 0;
                	dirM2 = 0;
                	pwmM3 = 0;
                	dirM3 = 0;
                	chute = 0;
                	drible = 0;
                	bateria = 0;
                	byte M1 = (byte) ((dirM1 * 128) + pwmM1);
                	byte M2 = (byte) ((dirM2 * 128) + pwmM2);
                	byte M3 = (byte) ((dirM3 * 128) + pwmM3);
                }else{
                	
                	System.out.println("Intensidade M1(0-100)");
                	pwmM1 = in.nextByte();
                	System.out.println("Direção M1 (0-1)");
                	dirM1 = in.nextByte();
                	byte M1 = (byte) ((dirM1 * 128) + pwmM1);
                	System.out.println("Intensidade M2(0-100)");
                	pwmM2 = in.nextByte();
                	System.out.println("Direção M2 (0-1)");
                	dirM2 = in.nextByte();
                	byte M2 = (byte) ((dirM2 * 128) + pwmM2);
                	System.out.println("Intensidade M3(0-100)");
                	pwmM3 = in.nextByte();
                	System.out.println("Direção M3 (0-1)");
                	dirM3 = in.nextByte();
                	byte M3 = (byte) ((dirM3 * 128) + pwmM3);
                	System.out.println("Chute : (0-2)");
                	chute = in.nextByte();
                	System.out.println("Drible: (0-1)");
                	drible = in.nextByte();
                	System.out.println("Bateria: (0-1)");
                	bateria = in.nextByte();
                }
                byte ultimo = (byte)(chute + (4*drible) + (8*bateria));
             	byte M1 = (byte) ((dirM1 * 128) + pwmM1);
            	byte M2 = (byte) ((dirM2 * 128) + pwmM2);
            	byte M3 = (byte) ((dirM3 * 128) + pwmM3);
                /** 		
				pwmM1 = (byte) 30;
				dirM1 = (byte) 0;
				pwmM2 = (byte) 50;
				dirM2 = (byte) 0;
				pwmM3 = (byte) 80;
				dirM3 = (byte) 00;
                */  
               
                byte[] arrayBytes = new byte[5];
                
                arrayBytes[0] = id;
                arrayBytes[1] = M1;
                arrayBytes[2] = M2;
                arrayBytes[3] = M3;
                arrayBytes[4] = ultimo;
              
              
                
                serialPort.writeBytes(arrayBytes);
                System.out.println("enviado");
                Thread.sleep(100);
//                if(times >= 48 && times <=57){
//                    Thread.sleep(50);
//                    try{
//                        byte[] asd = serialPort.readBytes(5, 200);
//                    }catch (SerialPortTimeoutException ex) {
//                       // Logger.getLogger(TesteFreedomboard.class.getName()).log(Level.SEVERE, null, ex);
//                        
//                        System.out.println("Timeout: "+ times);
//                        cont++;
//                    }
//                    
//                }
                
                times++;
                 
                
                
                
                
//                String ack = null;
                
//                while(ack == null){
//                 
//                    serialPort.writeBytes(send.getBytes());
//                    ack = serialPort.readString();  
//                }
                


//                ack = serialPort.readString(); 
//                System.out.println(ack);
                
            }                   
            serialPort.closePort();//Close serial port
//            erro = cont;
//            System.out.println("Erros:");
//            System.out.println(erro);
            
        }
        
        catch (SerialPortException ex) {
            System.out.println(ex);            
        }
        catch (InterruptedException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        } 
     
}
	
}
