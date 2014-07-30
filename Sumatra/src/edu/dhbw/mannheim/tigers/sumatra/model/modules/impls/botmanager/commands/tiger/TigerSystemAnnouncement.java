/*
 * *********************************************************
 * Copyright (c) 2009 - 2011, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: 03.03.2011
 * Author(s): AndreR
 * 
 * *********************************************************
 */
package edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.tiger;

import org.apache.log4j.Logger;

import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.ACommand;
import edu.dhbw.mannheim.tigers.sumatra.model.modules.impls.botmanager.commands.ECommand;
import edu.dhbw.mannheim.tigers.sumatra.util.serial.SerialData;
import edu.dhbw.mannheim.tigers.sumatra.util.serial.SerialData.ESerialDataType;


/**
 * System announcement from a bot.
 * Usually issued after a bot is switched on. Kind of notification for the
 * botmanager to send a SYSTEM_SET_IDENTITY command.
 * 
 * @author AndreR
 * 
 */
public class TigerSystemAnnouncement extends ACommand
{
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	// Logger
	private static final Logger	log		= Logger.getLogger(TigerSystemAnnouncement.class.getName());
	
	@SerialData(type = ESerialDataType.INT32)
	private final int					cpuId[]	= new int[3];
	
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * 
	 */
	public TigerSystemAnnouncement()
	{
		super(ECommand.CMD_SYSTEM_ANNOUNCEMENT);
	}
	
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	
	
	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Set CPU ID.
	 * Expected format is a 24 digit HEX number.
	 * 
	 * @param id CPU ID
	 */
	public void setCpuId(String id)
	{
		if (id.length() != 24)
		{
			log.error("Invalid CPU ID: " + id);
			return;
		}
		
		try
		{
			cpuId[2] = Integer.parseInt(id.substring(0, 8), 16);
			cpuId[1] = Integer.parseInt(id.substring(8, 16), 16);
			cpuId[0] = Integer.parseInt(id.substring(16, 24), 16);
		}
		
		catch (final NumberFormatException e)
		{
			log.error("Invalid CPU ID: " + id);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getCpuId()
	{
		String id;
		
		id = String.format("%08X", cpuId[2]);
		id += String.format("%08X", cpuId[1]);
		id += String.format("%08X", cpuId[0]);
		
		return id;
	}
}
