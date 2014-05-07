package comunication;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Pinho
 * 
 */

import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.Scanner;

public class Controller {

    public static void main(String[] args) {
        SerialPort serialPort = new SerialPort("\\\\\\\\.\\\\COM18"); //mudar porta
        boolean flag = false;
        Scanner in = new Scanner(System.in);
        try {
            serialPort.openPort();//Open serial port
                               serialPort.setParams(SerialPort.BAUDRATE_9600, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            //Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
            while(!flag){
    		System.out.println("Angulo (0~180):");
                int ang = in.nextInt();
                char angulo = (char) ang;
                System.out.println("ID:");
                int id = in.nextInt();
                System.out.println("Kick:");
                System.out.println("(0 - OFF)");
                System.out.println("(2 - ON FRACO)");
                System.out.println("(3 - ON FORTE)");
                int kick = in.nextInt();
                System.out.println("Drible (0-off,1-on):");
                int drible = in.nextInt();
                System.out.println("FUCKIN Sinal:");
                int sinal = in.nextInt();
//                System.out.println("Sinal: ");
//                int sinal = in.nextInt();
                System.out.println("Velocidade: (0~3)");
                int vel = in.nextInt();
                
                int resultado = vel + sinal*4 + drible*8 + kick*16 + id*64;
                char secbyte = (char) resultado;
                String first = ""+angulo;
                String second = ""+secbyte;
                
                System.out.println("Primeiro: " + ang);
                System.out.println("Segundo: "+resultado);
                
                
//                String ack = null;
                
//                while(ack == null){
//                 
//                    serialPort.writeBytes(send.getBytes());
//                    ack = serialPort.readString();  
//                }
                serialPort.writeInt(ang);
                serialPort.writeInt(resultado);
//                ack = serialPort.readString(); 
//                System.out.println(ack);
                
            }                   
            serialPort.closePort();//Close serial port
            
        }
        
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
}
    

