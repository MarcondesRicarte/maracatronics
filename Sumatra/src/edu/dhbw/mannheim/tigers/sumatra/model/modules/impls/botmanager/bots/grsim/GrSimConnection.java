/*
 * *********************************************************
 * Copyright (c) 2009 - 2013, Tigers DHBW Mannheim
 * Project: TIGERS - GrSimAdapter
 * Date: 17.07.2012
 * Author(s): Peter Birkenkampf, TilmanS
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.bots.grsim;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

import jssc.SerialPort;

import org.apache.log4j.Logger;

import edu.dhbw.mannheim.tigers.sumatra.model.data.GrSimCommands;
import edu.dhbw.mannheim.tigers.sumatra.model.data.GrSimPacket;


/**
 * Creates the Protobuf commands that are to be sent to grSim
 * 
 * @author Peter Birkenkampf, TilmanS
 */
public class GrSimConnection
{
	// --------------------------------------------------------------
	// --- instance-variables ---------------------------------------
	// --------------------------------------------------------------
	
	private static final Logger	log	= Logger.getLogger(GrSimConnection.class.getName());
	private DatagramSocket			ds;
	private String						ip;
	private int							port, id;
	private float						timeStamp, wheel1, wheel2, wheel3, wheel4, kickspeedx, kickspeedz, velx, vely, velz;
	private boolean					spinner, wheelSpeed, teamYellow;
	private int							kickmode;
	private boolean					kickerDisarm;
	private float						spinnerspeed;
	
	
	// --------------------------------------------------------------
	// --- constructor(s) -------------------------------------------
	// --------------------------------------------------------------
	
	/**
	 * @param config
	 */
	public GrSimConnection(final GrSimNetworkCfg config)
	{
		ip = config.getIp();
		port = config.getPort();
		teamYellow = config.isTeamYellow();
		
		timeStamp = 0;
		wheel1 = 0;
		wheel2 = 0;
		wheel3 = 0;
		wheel4 = 0;
		kickspeedx = 0;
		kickspeedz = 0;
		velx = 0;
		vely = 0;
		velz = 0;
		id = 0;
		spinner = false;
		wheelSpeed = false;
		kickmode = 1;
		kickerDisarm = true;
		spinnerspeed = 1400.0f;
		
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	/**
	 */
	public void open()
	{
		try
		{
			ds = new DatagramSocket();
		} catch (SocketException err)
		{
			log.error("Could not open datagram socket for grSimBot", err);
		}
	}
	
	
	/**
	 */
	public void close()
	{
		ds.close();
		ds = null;
	}
	
	
	/**
	 * @param timeStamp
	 */
	public void setTime(final float timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	
	/**
	 * @param wheel1
	 */
	public void setWheel1(final float wheel1)
	{
		this.wheel1 = wheel1;
	}
	
	
	/**
	 * @param wheel2
	 */
	public void setWheel2(final float wheel2)
	{
		this.wheel2 = wheel2;
	}
	
	
	/**
	 * @param wheel3
	 */
	public void setWheel3(final float wheel3)
	{
		this.wheel3 = wheel3;
	}
	
	
	/**
	 * @param wheel4
	 */
	public void setWheel4(final float wheel4)
	{
		this.wheel4 = wheel4;
	}
	
	
	/**
	 * @param mode
	 */
	public void setKickmode(final int mode)
	{
		kickmode = mode;
	}
	
	
	/**
	 * @param disarm
	 */
	public void setKickerDisarm(final boolean disarm)
	{
		kickerDisarm = disarm;
	}
	
	
	/**
	 * @param kickspeedx
	 */
	public void setKickspeedX(final float kickspeedx)
	{
		this.kickspeedx = kickspeedx;
	}
	
	
	/**
	 * @param kickspeedz
	 */
	public void setKickspeedZ(final float kickspeedz)
	{
		this.kickspeedz = kickspeedz;
	}
	
	
	/**
	 * @param velx
	 */
	public void setVelX(final float velx)
	{
		this.velx = velx;
	}
	
	
	/**
	 * @param vely
	 */
	public void setVelY(final float vely)
	{
		this.vely = vely;
	}
	
	
	/**
	 * @param velz
	 */
	public void setVelZ(final float velz)
	{
		this.velz = velz;
	}
	
	
	/**
	 * @param id
	 */
	public void setId(final int id)
	{
		this.id = id;
	}
	
	
	/**
	 * @param spinner
	 */
	public void setSpinner(final boolean spinner)
	{
		this.spinner = spinner;
	}
	
	
	/**
	 * @param speed
	 */
	
	public void setSpinnerSpeed(final float speed)
	{
		spinnerspeed = speed;
	}
	
	
	/**
	 * @param wheelSpeed
	 */
	public void setWheelSpeed(final boolean wheelSpeed)
	{
		this.wheelSpeed = wheelSpeed;
	}
	
	
	/**
	 * @param teamYellow
	 */
	public void setTeamYellow(final boolean teamYellow)
	{
		this.teamYellow = teamYellow;
	}
	
	
	/**
	 * @param ip
	 */
	
	public void setIp(final String ip)
	{
		this.ip = ip;
	}
	
	
	/**
	 * @param port
	 */
	public void setPort(final int port)
	{
		this.port = port;
	}
	
	
	/**
	 * Creates the protobuf command and sends it away
	 */
	public void send()
	{
		if (ds == null)
		{
			log.error("No open connection to grSim Bot.");
			return;
		}
		GrSimCommands.grSim_Robot_Command command = GrSimCommands.grSim_Robot_Command.newBuilder().setId(id)
				.setWheel2(wheel2).setWheel1(wheel1).setWheel3(wheel3).setWheel4(wheel4).setKickspeedx(kickspeedx)
				.setKickspeedz(kickspeedz).setVeltangent(velx).setVelnormal(vely).setVelangular(velz).setSpinner(spinner)
				.setWheelsspeed(wheelSpeed).setKickmode(kickmode).setDisarmKicker(kickerDisarm)
				.setSpinnerspeed(spinnerspeed).build();
		GrSimCommands.grSim_Commands command2 = GrSimCommands.grSim_Commands.newBuilder().setTimestamp(timeStamp)
				.setIsteamyellow(teamYellow).addRobotCommands(command).build();
		GrSimPacket.grSim_Packet packet = GrSimPacket.grSim_Packet.newBuilder().setCommands(command2).build();
		byte[] buffer2 = packet.toByteArray();
		try
		{
			DatagramPacket dp = new DatagramPacket(buffer2, buffer2.length, InetAddress.getByName(ip), port);
			ds.send(dp);
		} catch (IOException e)
		{
			log.error("Could not send package to grSim", e);
		}
		
		// Mandar comnados par rob� fisico teste
		Scanner in = new Scanner(System.in);
		SerialPort serialPort = new SerialPort("\\\\\\\\.\\\\COM3");
		try
		{
			if (serialPort.isOpened())
			{
				serialPort.closePort();
				System.out.println("Porta ocupada!");
			}
			serialPort.openPort();// Open serial port
			serialPort.setParams(SerialPort.BAUDRATE_9600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			
			byte pwmM1, pwmM2, pwmM3, dirM1, dirM2, dirM3, chute, drible, bateria;
			byte id = 0;
			System.out.println("velocidade:" + velx);
			if (velx != 0)
			{
				pwmM1 = 30;
				dirM1 = 0;
				pwmM2 = 50;
				dirM2 = 0;
				pwmM3 = 80;
				dirM3 = 0;
			} else
			{
				pwmM1 = 0;
				dirM1 = 0;
				pwmM2 = 0;
				dirM2 = 0;
				pwmM3 = 0;
				dirM3 = 0;
			}
			chute = 0;
			drible = 0;
			bateria = 0; // in.nextByte();
			byte ultimo = (byte) (chute + (4 * drible) + (8 * bateria));
			byte M1 = (byte) ((dirM1 * 128) + pwmM1);
			byte M2 = (byte) ((dirM2 * 128) + pwmM2);
			byte M3 = (byte) ((dirM3 * 128) + pwmM3);
			
			byte[] arrayBytes = new byte[5];
			
			arrayBytes[0] = id;
			arrayBytes[1] = M1;
			arrayBytes[2] = M2;
			arrayBytes[3] = M3;
			arrayBytes[4] = ultimo;
			
			for (int i = 0; i < 5; i++)
			{
				System.out.println(arrayBytes[i]);
			}
			System.out.println();
			serialPort.writeBytes(arrayBytes);
			System.out.println("enviado");
			serialPort.closePort();
			Thread.sleep(10000);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public String toString()
	{
		String s = "";
		s += "time: " + timeStamp + "\t";
		s += "ip: " + ip + "\t";
		s += "port: " + port + "\n";
		s += "team: ";
		if (teamYellow)
		{
			s += "Yellow";
		} else
		{
			s += "Blue";
		}
		s += "\tBotId " + id + "\n";
		if (wheelSpeed)
		{
			s += "wheel1: " + wheel1 + "\t";
			s += "wheel2: " + wheel2 + "\t";
			s += "wheel3: " + wheel3 + "\t";
			s += "wheel4: " + wheel4 + "\n";
		} else
		{
			s += "velX: " + velx + "\t";
			s += "velY: " + vely + "\t";
			s += "velZ: " + velz + "\n";
		}
		s += "kickerX: " + kickspeedx + "\t";
		s += "kickerZ: " + kickspeedz + "\t";
		s += "Spinner ";
		if (spinner)
		{
			s += "on";
		} else
		{
			s += "off";
		}
		return s;
	}
	
}
