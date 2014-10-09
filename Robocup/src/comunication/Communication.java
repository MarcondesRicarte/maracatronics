package comunication;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Communication {
    
    
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
                id = 0;
                pwmM1 = 30;
                dirM1 = 1;
                byte M1 = (byte) ((dirM1 * 128) + pwmM1);
                pwmM2 = 30;
                dirM2 = 0;
                byte M2 = (byte) ((dirM2 * 128) + pwmM2);
                pwmM3 = 0;
                dirM3 = 0;
                byte M3 = (byte) ((dirM3 * 128) + pwmM3);
                chute = 0;
                drible = 0;
                bateria = 0;
                byte ultimo = (byte)(chute + (4*drible) + (8*bateria));
                 
               
                byte[] arrayBytes = new byte[5];
                
                arrayBytes[0] = id;
                arrayBytes[1] = M1;
                arrayBytes[2] = M2;
                arrayBytes[3] = M3;
                arrayBytes[4] = ultimo;
              
              
                
                serialPort.writeBytes(arrayBytes);
                System.out.println("enviado");
                Thread.sleep(100);
                
                times++;
                                 
            }                   
            serialPort.closePort();//Close serial port

        }
        
        catch (SerialPortException ex) {
            System.out.println(ex);            
        }
        catch (InterruptedException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        } 
     
}
        
    }