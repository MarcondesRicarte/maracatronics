package comunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import robocup.messages.MessagesRobocupSslWrapper.SSL_WrapperPacket;
import data.Field;

public class CameraReader {

	private final String ADDRESS = "224.5.23.2";
	
	public final int PORT = 10020; 
	
	public void read(Field field){
		 String msg = "Hello";
		 InetAddress group;
//		 while(true){
			 try {
			 group = InetAddress.getByName(ADDRESS);

			 MulticastSocket s = new MulticastSocket(PORT);
			 s.joinGroup(group);
			 DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
					 InetAddress.getByName("127.0.0.1"), 20011); // group, PORT);
			 
			 s.send(hi);
			 // get their responses!
			 byte[] buf = new byte[1000];
			 DatagramPacket recv = new DatagramPacket(buf, buf.length);
			 s.receive(recv);
			 //System.out.println(s)
			 // OK, I'm done talking - leave the group...
			 s.leaveGroup(group);
			 
			 //continuando
            // Create a byte array with packet size.
			// System.out.println("Tamanho do array de bytes: " + recv.getLength()); 
            byte[] a = new byte[recv.getLength()];
            System.arraycopy(buf, 0, a, 0, recv.getLength());

            
            // Parse the received message
            SSL_WrapperPacket parsEFrom = SSL_WrapperPacket.parseFrom(a);
			
            ParserReader.parse(parsEFrom, field);
            
			 } catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();

			 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }	catch (Exception e){
				 System.out.print(e.getMessage());
			 }
	}
	
	
	public static void main(String[] args) {
		CameraReader cr = new CameraReader();
		Field field = new Field();
		cr.read(field);		
	}
	
}
